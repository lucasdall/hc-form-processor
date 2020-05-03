package life.heartcare.formprocessor.service;

import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import life.heartcare.formprocessor.dto.FormResponseDTO;
import life.heartcare.formprocessor.dto.mailchimp.CreateMemberRequestDTO;
import life.heartcare.formprocessor.dto.mailchimp.enums.Language;
import life.heartcare.formprocessor.dto.mailchimp.enums.MergeField;
import life.heartcare.formprocessor.dto.mailchimp.enums.Status;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MailchimpService {

	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${mailchimp.api.url.members.create}")
	private String URL_LIST_MEMBERS_CREATE;

	@Value("${mailchimp.api.user}")
	private String MAILCHIMP_API_USER;

	@Value("${mailchimp.api.pwd}")
	private String MAILCHIMP_API_PWD;

	public String createMember(FormResponseDTO form) {
		try {
			log.info("begin - createMember");
			CreateMemberRequestDTO body = CreateMemberRequestDTO.builder()
					.emailAddress(form.getEmail())
					.status(Status.subscribed)
					.language(Language.PT)
					.mergeField(MergeField.FNAME, form.getName())
					.mergeField(MergeField.PHONE, form.getPhone())
					.mergeField(MergeField.RESULT, form.getResult().name())
					.mergeField(MergeField.COUNTRY, form.getCountry())
					.build();
			
			String emailMd5 = DigestUtils.md5Hex(form.getEmail().toLowerCase()).toLowerCase();
			
			HttpHeaders headers = new HttpHeaders();
			headers.setBasicAuth(MAILCHIMP_API_USER, MAILCHIMP_API_PWD);		
			HttpEntity<CreateMemberRequestDTO> entity = new HttpEntity<CreateMemberRequestDTO>(body, headers);
			
			ResponseEntity<Map<String, Object>> response = restTemplate.exchange(URL_LIST_MEMBERS_CREATE, HttpMethod.PUT,  entity, new ParameterizedTypeReference<Map<String,Object>>() {}, emailMd5);
			if (response.getStatusCode().is2xxSuccessful()) {
				Map<String, Object> respBody = response.getBody();
				String mailchimpId = (String) respBody.get("id");
				log.info("mailchimp - created - id [{}] email_address [{}]", mailchimpId, respBody.get("email_address"));
				return mailchimpId;
			} else {
				return null;
			}
		} catch (Exception e) {
			log.error("Error createMember", e);
			return null;
		} finally {
			log.info("end - createMember");
		}
	}
	
	

}