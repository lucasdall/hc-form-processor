package life.heartcare.formprocessor.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import life.heartcare.formprocessor.dto.FormResponseDTO;
import life.heartcare.formprocessor.service.FormResponseService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/formprocessor")
@Slf4j
public class FormProcessorController {

	@Autowired
	private FormResponseService formResponseService;

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

	@GetMapping(path = "/findlatest/byemail/{email}")
	public ResponseEntity<FormResponseDTO> findLatestByEmail(@PathVariable("email") String email) throws Exception {
		log.info("begin - findLatestByEmail - email[{}]", email);
		try {
			return ResponseEntity.ok(formResponseService.findTop1ByEmail(email));
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

}
