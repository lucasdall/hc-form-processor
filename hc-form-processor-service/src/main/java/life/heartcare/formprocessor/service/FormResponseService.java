package life.heartcare.formprocessor.service;

import java.time.LocalDateTime;
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
		FormResponse fp = formResponseRepository.findTop1ByEmailOrderByIdFormResponseDesc(email);
		LocalDateTime lastMinute = life.heartcare.formprocessor.dto.utils.DateUtils.convertToLocalDateTimeViaInstant(DateUtils.addMinutes(new Date(), -1));
		Boolean hasNewResponse = Boolean.FALSE;
		if (fp != null) {
			LocalDateTime submitedAt = life.heartcare.formprocessor.dto.utils.DateUtils.convertToLocalDateTimeViaInstant(fp.getSubmittedAt());
			log.info("findTop1ByEmail - lastMinute[{}]", lastMinute);
			log.info("findTop1ByEmail - formResponse.submitedAt[{}]", submitedAt);
			if (submitedAt.isAfter(lastMinute)) {
				log.info("findTop1ByEmail - new response id [{}]", fp.getIdFormResponse());
				hasNewResponse = Boolean.TRUE;
			} else {
				log.info("findTop1ByEmail - theres no new response for [{}], the ondest one was id[{}] result[{}] submitedAt[{}]", email, fp.getIdFormResponse(), fp.getResult(), submitedAt);
			}
		}
		
		if (hasNewResponse) {
			CheckResponseDTO resp = new CheckResponseDTO();
			resp.setIdFormResponse(fp.getIdFormResponse());
			resp.setEmail(email);
			resp.setIdFormResponse(fp.getIdFormResponse());
			resp.setFound(Boolean.TRUE);
			return resp;
		} else {
			CheckResponseDTO resp = new CheckResponseDTO();
			resp.setEmail(email);
			resp.setFound(Boolean.FALSE);
			Boolean lastAttempt = Boolean.FALSE;
			if (retryAttempt != null) {
				resp.setRetryAttempt(--retryAttempt);
				if (retryAttempt <= 0) {
					lastAttempt = true;
				}
			}
			if (retryTimeout != null) {
				resp.setRetryTimeout(retryTimeout);
			}
			if (!lastAttempt) {
				resp.setLink(String.format("/api/formprocessor/findlatest/byemail/%s/%s/%s", email, resp.getRetryAttempt(), resp.getRetryTimeout()));
			} else {
				resp.setFinished(Boolean.TRUE);
				resp.setLink("/api/formprocessor/result/notfound");
			}
			return resp;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public FormResponseDTO webhookSave(String payload, String contentType) throws Exception {
		log.info("begin - webhookSave - contentType[{}]", contentType);
		String email = null;
		try {
			Map<String, Object> payloadMap = objectMapper.readValue(payload, new TypeReference<Map<String, Object>>() {});
			FormResponse entity = new FormResponse();
			entity.setContentType(contentType);
			entity.setEventId((String) payloadMap.get("event_id"));
			entity.setEventType((String) payloadMap.get("event_type"));
			entity.setPayload(payload);
			Map<String, Object> formResponse = (Map<String, Object>) payloadMap.get("form_response");
			if (formResponse != null) {
				if (formResponse.containsKey("submitted_at")) {
					entity.setSubmittedAt(DateFormatUtils.ISO_8601_EXTENDED_DATETIME_FORMAT.parse((String) formResponse.get("submitted_at")));					
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

}
