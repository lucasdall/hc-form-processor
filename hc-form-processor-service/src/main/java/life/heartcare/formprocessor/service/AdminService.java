package life.heartcare.formprocessor.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import life.heartcare.formprocessor.dto.AnswerDTO;
import life.heartcare.formprocessor.dto.AnswerListDTO;
import life.heartcare.formprocessor.dto.DetailAnswersDTO;
import life.heartcare.formprocessor.dto.FormResponseDTO;
import life.heartcare.formprocessor.dto.admin.SummaryDTO;
import life.heartcare.formprocessor.dto.enums.QuestionsLabelsId;
import life.heartcare.formprocessor.dto.enums.Results;
import life.heartcare.formprocessor.persistence.FormResponseRepository;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AdminService {

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private FormResponseRepository formResponseRepository;
	
	@Autowired
	private FormResponseService formResponseService;
	
	@Autowired
	private ObjectMapper objectMapper;	
	
	@Transactional
	public List<SummaryDTO> countByType() {
		List<SummaryDTO> summaries = new ArrayList<SummaryDTO>();
		List<Map<String, Object>> map = formResponseRepository.countByType();
		map.forEach(m -> {
			summaries.add(SummaryDTO
				.builder()
				.count((Long) m.get("count"))
				.result((Results)m.get("result"))
				.build());
		});
		return summaries;
	}
	
	@Transactional
	public List<FormResponseDTO> findAllByResult(Results result) {
		return modelMapper.map(formResponseRepository.findAllByResultOrderByIdFormResponseDesc(result), new TypeToken<List<FormResponseDTO>>() {}.getType());
	}
	

	@SuppressWarnings("unchecked")
	@Transactional
	public DetailAnswersDTO detailAnswers(Long id) {
		try {
			FormResponseDTO FormResponse = formResponseService.findById(id);
			Map<String, Object> payloadMap = objectMapper.readValue(FormResponse.getPayload(), new TypeReference<Map<String, Object>>() {});
			Map<String, Object> formResponse = (Map<String, Object>) payloadMap.get("form_response");
			if (formResponse != null) {
				List<Map<String, Object>> answersList = (List<Map<String, Object>>) formResponse.get("answers");
				AnswerListDTO answers = new AnswerListDTO(objectMapper.convertValue(answersList, new TypeReference<List<AnswerDTO>>() {}));
				if (answers != null) {
					List<String> refs = Arrays.asList(
							QuestionsLabelsId.HC_TEST.toString(), 
							QuestionsLabelsId.HC_SYMPTOMS_TYPE.toString(), 
							QuestionsLabelsId.HC_SYMPTOMS_OTHERS.toString(), 
							QuestionsLabelsId.HC_SYMPTOMS_BREATHE.toString(), 
							QuestionsLabelsId.HC_SYMPTOMS_CRITICAL.toString(), 
							QuestionsLabelsId.HC_COVID_RECOVERED.toString(), 
							QuestionsLabelsId.HC_PROTECTED_PARTNERS.toString());
					List<AnswerDTO> l = answers.list();
					l.stream().filter(aa -> refs.contains(aa.getField().getRef())).forEach(a -> a.setClassStyle("has-background-warning"));
					
					return DetailAnswersDTO
							.builder()
							.FormResponse(FormResponse)
							.answers(l)
							.build();
				}
			}
		} catch (Exception e) {
			log.error("ERROR - detailAnswers", e);
		}
		return null;
	}


}