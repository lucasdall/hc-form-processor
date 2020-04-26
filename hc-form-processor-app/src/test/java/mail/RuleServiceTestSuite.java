//package mail;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
//import org.springframework.context.annotation.Import;
//import org.springframework.test.context.ActiveProfiles;
//
//import life.heartcare.formprocessor.FormProcessorApplication;
//import life.heartcare.formprocessor.api.cfg.FreemarkerCfg;
//import life.heartcare.formprocessor.api.mail.EmailService;
//import life.heartcare.formprocessor.dto.FormResponseDTO;
//import life.heartcare.formprocessor.dto.FormResponseEmailDTO;
//import life.heartcare.formprocessor.service.FormResponseService;
//
//@SpringBootTest(classes = FormProcessorApplication.class, webEnvironment = WebEnvironment.NONE)
//@Import({FreemarkerCfg.class})
//@ActiveProfiles("dev")
//public class RuleServiceTestSuite {
//
//	@Autowired
//	private EmailService emailService;
//	
//	@Autowired
//	private FormResponseService formResponseService;
//	
//	@Test
//	public void sendMail() throws Exception {
//		FormResponseDTO dto = formResponseService.findByEmailOrderByIdFormResponseDesc("lucasdall@gmail.com").get(0);
//		FormResponseEmailDTO emailDTO = formResponseService.convertFrom(dto);
//		emailService.sendCovidMessage(emailDTO);
//	}
//
//}
