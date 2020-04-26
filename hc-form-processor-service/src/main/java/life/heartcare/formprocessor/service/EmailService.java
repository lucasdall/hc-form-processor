package life.heartcare.formprocessor.service;

import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.email.EmailBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import freemarker.template.Template;
import life.heartcare.formprocessor.dto.AnswerDTO;
import life.heartcare.formprocessor.dto.AnswerListDTO;
import life.heartcare.formprocessor.dto.FormResponseDTO;
import life.heartcare.formprocessor.dto.FormResponseEmailDTO;
import life.heartcare.formprocessor.dto.enums.QuestionsLabelsId;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class EmailService {

	@Autowired
	private Mailer customMailer;
	
	@Autowired
	private Template emailResult;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private ModelMapper modelMapper;

	public void sendCovidMessage(FormResponseDTO dto) throws Exception {
		log.info("begin - sendCovidMessage [{}] result [{}]", dto.getEmail(), dto.getResult());
		FormResponseEmailDTO emailDTO = convertFrom(dto);
		String html = FreeMarkerTemplateUtils.processTemplateIntoString(emailResult, emailDTO);
		Email email = EmailBuilder.startingBlank()
				.to(dto.getEmail())
				.withSubject("Suas recomendações para COVID-19")
				.withHTMLText(html).buildEmail();
		customMailer.sendMail(email, true);
		log.info("end - sendCovidMessage [{}] result [{}]", dto.getEmail(), dto.getResult());
	}

	@SuppressWarnings("unchecked")
	private FormResponseEmailDTO convertFrom(FormResponseDTO formRespDTO) throws Exception {
		FormResponseEmailDTO emailDTO = modelMapper.map(formRespDTO, FormResponseEmailDTO.class);
		String payload = emailDTO.getPayload();
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
					emailDTO.setName(name.getText());
				}
				AnswerDTO hcSymptomsType = answers.getById(QuestionsLabelsId.HC_SYMPTOMS_TYPE);
				if (hcSymptomsType != null) {
					emailDTO.getSymptoms().addAll(hcSymptomsType.getChoices().getLabels());
				}
				AnswerDTO hcSymptomsOthers = answers.getById(QuestionsLabelsId.HC_SYMPTOMS_OTHERS);
				if (hcSymptomsOthers != null) {
					emailDTO.getSymptoms().addAll(hcSymptomsOthers.getChoices().getLabels());
				}
				emailDTO.getSymptoms().removeIf(s -> "nenhum destes".equalsIgnoreCase(s));
			}
		}
		return emailDTO;
	}
}