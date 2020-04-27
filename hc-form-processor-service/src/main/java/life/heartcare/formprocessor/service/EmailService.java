package life.heartcare.formprocessor.service;

import java.util.HashMap;
import java.util.Map;

import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.email.EmailBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import freemarker.template.Template;
import life.heartcare.formprocessor.dto.FormResponseDTO;
import life.heartcare.formprocessor.dto.FormResponseResultDTO;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class EmailService {

	@Autowired
	private FormResponseService formResponseService;
	
	@Autowired
	private Mailer customMailer;
	
	@Autowired
	private Template emailResult;
	
	public void sendCovidMessage(FormResponseDTO dto) throws Exception {
		log.info("begin - sendCovidMessage [{}] result [{}]", dto.getEmail(), dto.getResult());
		FormResponseResultDTO emailDTO = formResponseService.convertFrom(dto);
		Map<String, Object> map = new HashMap<>();
		map.put("dto", emailDTO);
		String html = FreeMarkerTemplateUtils.processTemplateIntoString(emailResult, map);
		Email email = EmailBuilder.startingBlank()
				.to(dto.getEmail())
				.withSubject("Suas recomendações para COVID-19")
				.withHTMLText(html).buildEmail();
		customMailer.sendMail(email, true);
		log.info("end - sendCovidMessage [{}] result [{}]", dto.getEmail(), dto.getResult());
	}
}