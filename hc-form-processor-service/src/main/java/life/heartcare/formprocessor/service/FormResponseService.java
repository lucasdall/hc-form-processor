package life.heartcare.formprocessor.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import life.heartcare.formprocessor.dto.AnswerDTO;
import life.heartcare.formprocessor.dto.AnswerListDTO;
import life.heartcare.formprocessor.dto.CheckResponseDTO;
import life.heartcare.formprocessor.dto.FormResponseDTO;
import life.heartcare.formprocessor.dto.FormResponseResultDTO;
import life.heartcare.formprocessor.dto.FormResponseResumedDTO;
import life.heartcare.formprocessor.dto.enums.QuestionsLabelsId;
import life.heartcare.formprocessor.dto.enums.Results;
import life.heartcare.formprocessor.model.FormResponse;
import life.heartcare.formprocessor.persistence.FormResponseRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FormResponseService {

	@Autowired
	FormResponseRepository formResponseRepository;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	RulesService rulesService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private MailchimpService mailchimpService;
	
	@Transactional
	public List<FormResponseDTO> findByEmailOrderByIdFormResponseDesc(String email) {
		return modelMapper.map(formResponseRepository.findByEmailOrderByIdFormResponseDesc(email), new TypeToken<List<FormResponseDTO>>() {}.getType());
	}

	@Transactional
	public FormResponseDTO findById(Long id) {
		FormResponse entity = formResponseRepository.findById(id).get();
		FormResponseDTO dto = modelMapper.map(entity, FormResponseDTO.class); 
		return dto;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public FormResponseDTO check(Long id) throws Exception {
		log.info("begin - check - idFormResponse[{}]", id);
		FormResponseDTO dto = null;
		FormResponse entity = formResponseRepository.findById(id).get();
		Map<String, Object> payloadMap = objectMapper.readValue(entity.getPayload(), new TypeReference<Map<String, Object>>() {});
		Map<String, Object> formResponse = (Map<String, Object>) payloadMap.get("form_response");
		if (formResponse != null) {
			List<Map<String, Object>> answersList = (List<Map<String, Object>>) formResponse.get("answers");
			AnswerListDTO answers = new AnswerListDTO(objectMapper.convertValue(answersList, new TypeReference<List<AnswerDTO>>() {}));
			if (answers != null) {
				Results result = rulesService.execute(answers);
				if (!result.equals(entity.getResult())) {
					log.info("check - idFormResponse[{}] - changing FROM[{}] -> TO[{}]", id, entity.getResult(), result);
					entity.setResult(result);
					formResponseRepository.save(entity);
				}
				dto = modelMapper.map(entity, FormResponseDTO.class);
				emailService.sendCovidMessage(dto);
			}
		}
		log.info("end - check - idFormResponse[{}]", id);
		return dto;
	}

	@Transactional
	public FormResponseResumedDTO findTop1ByEmail(String email) {
		FormResponse fp = formResponseRepository.findTop1ByEmailOrderByIdFormResponseDesc(email);
		if (fp != null) {
			return modelMapper.map(fp, FormResponseResumedDTO.class);
		}
		return null;
	}


	@Transactional
	public CheckResponseDTO checkResponse(String email, Integer retryAttempt, Integer retryTimeout) {
		log.info("begin - email[{}] retryAttempt[{}] retryTimeout[{}]", email, retryAttempt, retryTimeout);
		FormResponse fp = formResponseRepository.findTop1ByEmailOrderByIdFormResponseDesc(email);
		Date lastMinute = DateUtils.addMinutes(new Date(), -1);
		Boolean hasNewResponse = Boolean.FALSE;
		if (fp != null) {
			Date savedAt = fp.getSavedAt();
			log.info("lastMinute[{}]", lastMinute);
			log.info("formResponse.submitedAt[{}]", savedAt);
			if (fp.getSavedAt().after(lastMinute)) {
				log.info("new response id [{}]", fp.getIdFormResponse());
				hasNewResponse = Boolean.TRUE;
			} else {
				log.info("theres no new response for [{}], the ondest one was id[{}] result[{}] submitedAt[{}]", email, fp.getIdFormResponse(), fp.getResult(), savedAt);
			}
		}
		
		Boolean lastAttempt = Boolean.FALSE;
		CheckResponseDTO resp = new CheckResponseDTO();
		if (hasNewResponse) {
			resp.setIdFormResponse(fp.getIdFormResponse());
			resp.setEmail(email);
			resp.setFound(Boolean.TRUE);
		} else {
			resp.setEmail(email);
			resp.setFound(Boolean.FALSE);
		}
		if (retryAttempt != null) {
			resp.setRetryAttempt(--retryAttempt);
			if (retryAttempt <= 0) {
				lastAttempt = true;
			}
		}
		if (retryTimeout != null) {
			resp.setRetryTimeout(retryTimeout);
		}
		if (lastAttempt) {
			resp.setLink("/api/formprocessor/result/notfound");
			resp.setFinished(Boolean.TRUE);
		} else if (resp.getFound()){
			resp.setLink(String.format("/api/formprocessor/result/%s", email));
		} else {
			resp.setLink(String.format("/api/formprocessor/findlatest/byemail/%s/%s/%s", email, resp.getRetryAttempt(), resp.getRetryTimeout()));
		}
		log.info("link [{}]", resp.getLink());
		log.info("end - email[{}] retryAttempt[{}] retryTimeout[{}]", email, retryAttempt, retryTimeout);
		return resp;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public FormResponseDTO webhookSave(String payload, String contentType) throws Exception {
		log.info("begin - webhookSave - contentType[{}]", contentType);
		String email = null;
		String name = null;
		String phone = null;
		String country = null;
		try {
			Map<String, Object> payloadMap = objectMapper.readValue(payload, new TypeReference<Map<String, Object>>() {});
			FormResponse entity = new FormResponse();
			entity.setContentType(contentType);
			entity.setEventId((String) payloadMap.get("event_id"));
			entity.setEventType((String) payloadMap.get("event_type"));
			entity.setPayload(payload);
			entity.setSavedAt(new Date());
			Map<String, Object> formResponse = (Map<String, Object>) payloadMap.get("form_response");
			if (formResponse != null) {
				if (formResponse.containsKey("submitted_at")) {
					entity.setSubmittedAt(DateFormatUtils.ISO_8601_EXTENDED_DATETIME_TIME_ZONE_FORMAT.parse((String) formResponse.get("submitted_at")));					
				}
				entity.setFormId((String) formResponse.get("form_id"));
				
				List<Map<String, Object>> answersList = (List<Map<String, Object>>) formResponse.get("answers");
				AnswerListDTO answers = new AnswerListDTO(objectMapper.convertValue(answersList, new TypeReference<List<AnswerDTO>>() {}));
				if (answers != null) {
					AnswerDTO hcEmail = answers.getById(QuestionsLabelsId.HC_EMAIL);
					if (hcEmail != null) {
						email = hcEmail.getEmail(); 
						log.info("email found in payload [{}]", email);
						entity.setEmail(email);
					}

					AnswerDTO hcName = answers.getById(QuestionsLabelsId.HC_NAME);
					if (hcName != null) {
						name = hcName.getText();
						log.info("name found in payload [{}]", name);
						entity.setName(name);
					}

					AnswerDTO hcPhone = answers.getById(QuestionsLabelsId.HC_PHONE);
					if (hcPhone != null) {
						phone = hcPhone.getPhoneNumber();
						log.info("phone found in payload [{}]", phone);
						entity.setPhone(phone);
					}

					AnswerDTO hcCountry = answers.getById(QuestionsLabelsId.HC_COUNTRY);
					if (hcCountry != null) {
						country = hcCountry.getChoice().getLabel();
						log.info("country found in payload [{}]", country);
						entity.setCountry(country);
					}

					Results result = rulesService.execute(answers);
					entity.setResult(result);
				}
			}
			
			formResponseRepository.save(entity);
			FormResponseDTO dto = modelMapper.map(entity, FormResponseDTO.class);
			
			try {
				emailService.sendCovidMessage(dto);				
			} catch (Exception e) {
				log.error("ERROR - webhookSave - send email failure - email[{}] idFormResponse[{}]", email, dto.getIdFormResponse());
				log.error("ERROR - webhookSave - send email failure", e);
			}

			try {
				String mailchimpId = mailchimpService.createMember(dto);
				if (mailchimpId != null) {
					log.info("mailchimp subscription [OK]");
					entity.setMailchimpId(mailchimpId);
					formResponseRepository.save(entity);
				} else {
					log.info("mailchimp subscription [FAIL]");
				}
			} catch (Exception e) {
				log.error("ERROR - mailchimp - member subscription", e);
			}
			return dto;
		} catch (Exception e) {
			log.error("ERROR - webhookSave - email[{}] contentType[{}]", email, contentType);
			log.error("ERROR - webhookSave", e);
			throw e;
		} finally {
			log.info("end - webhookSave");
		}
	}

	@SuppressWarnings("unchecked")
	public FormResponseResultDTO convertFrom(FormResponseDTO formRespDTO) throws Exception {
		FormResponseResultDTO resultDTO = modelMapper.map(formRespDTO, FormResponseResultDTO.class);
		String payload = resultDTO.getPayload();
		Map<String, Object> payloadMap = objectMapper.readValue(payload, new TypeReference<Map<String, Object>>() {
		});
		Map<String, Object> formResponse = (Map<String, Object>) payloadMap.get("form_response");
		if (formResponse != null) {
			List<Map<String, Object>> answersList = (List<Map<String, Object>>) formResponse.get("answers");
			AnswerListDTO answers = new AnswerListDTO(objectMapper.convertValue(answersList, new TypeReference<List<AnswerDTO>>() {
			}));
			if (answers != null) {
				AnswerDTO name = answers.getById(QuestionsLabelsId.HC_NAME);
				if (name != null) {
					resultDTO.setName(name.getText());
				}
				AnswerDTO hcSymptomsType = answers.getById(QuestionsLabelsId.HC_SYMPTOMS_TYPE);
				if (hcSymptomsType != null) {
					resultDTO.getSymptoms().addAll(hcSymptomsType.getChoices().getLabels());
				}
				AnswerDTO hcSymptomsOthers = answers.getById(QuestionsLabelsId.HC_SYMPTOMS_OTHERS);
				if (hcSymptomsOthers != null) {
					resultDTO.getSymptoms().addAll(hcSymptomsOthers.getChoices().getLabels());
				}
				resultDTO.getSymptoms().removeIf(s -> "nenhum destes".equalsIgnoreCase(s));
			}
		}
		resultDTO.loadDetail();
		return resultDTO;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public FormResponseDTO mailchimp(FormResponse entity) throws Exception {
		log.info("begin - mailchimp - contentType[{}]", entity.getContentType());
		String email = null;
		String name = null;
		String phone = null;
		String country = null;
		try {
			if (entity.getPayload() == null) {
				return null;
			}
			Map<String, Object> payloadMap = objectMapper.readValue(entity.getPayload(), new TypeReference<Map<String, Object>>() {});
			Map<String, Object> formResponse = (Map<String, Object>) payloadMap.get("form_response");
			if (formResponse != null) {
				List<Map<String, Object>> answersList = (List<Map<String, Object>>) formResponse.get("answers");
				AnswerListDTO answers = new AnswerListDTO(objectMapper.convertValue(answersList, new TypeReference<List<AnswerDTO>>() {}));
				if (answers != null) {
					AnswerDTO hcEmail = answers.getById(QuestionsLabelsId.HC_EMAIL);
					if (hcEmail != null) {
						email = hcEmail.getEmail(); 
						log.info("email found in payload [{}]", email);
						entity.setEmail(email);
					}

					AnswerDTO hcName = answers.getById(QuestionsLabelsId.HC_NAME);
					if (hcName != null) {
						name = hcName.getText();
						log.info("name found in payload [{}]", name);
						entity.setName(name);
					}

					AnswerDTO hcPhone = answers.getById(QuestionsLabelsId.HC_PHONE);
					if (hcPhone != null) {
						phone = hcPhone.getPhoneNumber();
						log.info("phone found in payload [{}]", phone);
						entity.setPhone(phone);
					}

					AnswerDTO hcCountry = answers.getById(QuestionsLabelsId.HC_COUNTRY);
					if (hcCountry != null) {
						country = hcCountry.getChoice().getLabel();
						log.info("country found in payload [{}]", country);
						entity.setCountry(country);
					}

					Results result = rulesService.execute(answers);
					entity.setResult(result);
				}
			}
			
			formResponseRepository.save(entity);
			FormResponseDTO dto = modelMapper.map(entity, FormResponseDTO.class);
			
			try {
				String mailchimpId = mailchimpService.createMember(dto);
				if (mailchimpId != null) {
					log.info("mailchimp subscription [OK]");
					entity.setMailchimpId(mailchimpId);
					formResponseRepository.save(entity);
				} else {
					log.info("mailchimp subscription [FAIL]");
				}
			} catch (Exception e) {
				log.error("ERROR - mailchimp - member subscription", e);
			}
			return dto;
		} catch (Exception e) {
			log.error("ERROR - mailchimp - email[{}] contentType[{}]", email, entity.getContentType());
			log.error("ERROR - mailchimp", e);
			throw e;
		} finally {
			log.info("end - mailchimp");
		}
	}
	
	
}
