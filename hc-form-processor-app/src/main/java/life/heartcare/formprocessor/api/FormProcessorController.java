package life.heartcare.formprocessor.api;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import life.heartcare.formprocessor.dto.CheckResponseDTO;
import life.heartcare.formprocessor.dto.FormResponseDTO;
import life.heartcare.formprocessor.dto.FormResponseResultDTO;
import life.heartcare.formprocessor.dto.enums.Results;
import life.heartcare.formprocessor.persistence.FormResponseRepository;
import life.heartcare.formprocessor.service.FormResponseService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/formprocessor")
@Slf4j
public class FormProcessorController {

	@Autowired
	private FormResponseService formResponseService;
	
	@Autowired
	private FormResponseRepository repo;
	
	@GetMapping(path = "/{id}")
	public ResponseEntity<FormResponseDTO> findById(@PathVariable("id") Long id) throws Exception {
		log.info("begin - findById - id[{}]", id);
		try {
			return ResponseEntity.ok(formResponseService.findById(id));
		} catch (Exception e) {
			log.error("ERROR - findById - id[{}]", id);
			log.error("ERROR - findById", e);
			throw e;
		} finally {
			log.info("end - findById - id[{}]", id);
		}
	}

	@GetMapping(path = "/{id}/check")
	public ResponseEntity<FormResponseDTO> check(@PathVariable("id") Long id) throws Exception {
		log.info("begin - check - id[{}]", id);
		try {
			return ResponseEntity.ok(formResponseService.check(id));
		} catch (Exception e) {
			log.error("ERROR - check - id[{}]", id);
			log.error("ERROR - check", e);
			throw e;
		} finally {
			log.info("end - check - id[{}]", id);
		}
	}

	@GetMapping(path = {"/findlatest/byemail/{email}", "/findlatest/byemail/{email}/{retryAttempt}/{retryTimeout}"})
	public ResponseEntity<CheckResponseDTO> findLatestByEmail(@PathVariable("email") String email, @PathVariable(name = "retryTimeout", required = false) Integer retryTimeout, @PathVariable(name = "retryAttempt", required = false) Integer retryAttempt) throws Exception {
		log.info("begin - findLatestByEmail - email[{}]", email);
		try {
			CheckResponseDTO dto = formResponseService.checkResponse(email, retryAttempt, retryTimeout);
			return ResponseEntity.ok(dto);
		} catch (Exception e) {
			log.error("ERROR - findLatestByEmail - email[{}]", email);
			log.error("ERROR - findLatestByEmail", e);
			throw e;
		} finally {
			log.info("end - findLatestByEmail - email[{}]", email);
		}
	}
	
	@GetMapping(path = "/findall/byemail/{email}")
	public ResponseEntity<List<FormResponseDTO>> findAllByEmail(@PathVariable("email") String email) throws Exception {
		log.info("begin - findAllByEmail - email[{}]", email);
		try {
			return ResponseEntity.ok(formResponseService.findByEmailOrderByIdFormResponseDesc(email));
		} catch (Exception e) {
			log.error("ERROR - findAllByEmail - email[{}]", email);
			log.error("ERROR - findAllByEmail", e);
			throw e;
		} finally {
			log.info("end - findAllByEmail - email[{}]", email);
		}
	}

	@PostMapping(path = "/webhook")
	public ResponseEntity<Void> webhook(@RequestBody String payload, @RequestHeader(name = HttpHeaders.CONTENT_TYPE) String contentType) throws Exception {
		log.info("begin - webhook - contentType[{}]", contentType);
		try {
			formResponseService.webhookSave(payload, contentType);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			log.error("ERROR - webhook - contentType[{}]", contentType);
			log.error("ERROR - webhook", e);
			throw e;
		} finally {
			log.info("end - webhook - contentType[{}]", contentType);
		}
	}
	
	@GetMapping(path = "/result/{email}")
	public ModelAndView result(@PathVariable String email) throws Exception {
		CheckResponseDTO dto = formResponseService.checkResponse(email, 10, Long.valueOf(TimeUnit.SECONDS.toMillis(5)).intValue());
		if (dto != null && dto.getFound()) {
			FormResponseResultDTO dtoResp = formResponseService.convertFrom(formResponseService.findById(dto.getIdFormResponse()));
			dtoResp.setChannel("web");
			return new ModelAndView("result", "dto", dtoResp);
		} else {
			FormResponseResultDTO dtoResp = new FormResponseResultDTO();
			dtoResp.setEmail(email);
			dtoResp.setChannel("web");
			dtoResp.setLink(String.format("/api/formprocessor/findlatest/byemail/%s/%s/%s", email, dto.getRetryAttempt(), dto.getRetryTimeout()));
			return new ModelAndView("processing", "dto", dtoResp);
		}
	}

	@GetMapping(path = "/mailchimp")
	@Transactional
	public String mailchimp() throws Exception {
		repo.findAll().forEach(f -> {
			try {
				if (StringUtils.isEmpty(f.getMailchimpId())) {
					formResponseService.mailchimp(f);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		return "ok";
	}

	@GetMapping(path = "/result/notfound")
	public ModelAndView resultNotFound() throws Exception {
		FormResponseResultDTO dtoResp = new FormResponseResultDTO();
		dtoResp.setResult(Results.TYPE_11_Unknown);
		dtoResp.setName("Prezado");
		dtoResp.loadDetail();
		dtoResp.setChannel("web");
		return new ModelAndView("result", "dto", dtoResp);
	}

}
