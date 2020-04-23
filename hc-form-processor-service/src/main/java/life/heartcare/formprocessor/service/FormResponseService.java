package life.heartcare.formprocessor.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import life.heartcare.formprocessor.dto.AnswerDTO;
import life.heartcare.formprocessor.dto.AnswerListDTO;
import life.heartcare.formprocessor.dto.FormResponseDTO;
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
			}
		}
		log.info("end - check - idFormResponse[{}]", id);
		return dto;
	}

	@Transactional
	public FormResponseDTO findTop1ByEmail(String email) {
		return modelMapper.map(formResponseRepository.findTop1ByEmail(email), FormResponseDTO.class);
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
			return modelMapper.map(entity, FormResponseDTO.class);
		} catch (Exception e) {
			log.error("ERROR - webhookSave - email[{}] contentType[{}]", email, contentType);
			log.error("ERROR - webhookSave", e);
			throw e;
		} finally {
			log.info("end - webhookSave");
		}
	}

}
