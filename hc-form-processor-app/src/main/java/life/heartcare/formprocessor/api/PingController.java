package life.heartcare.formprocessor.api;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {

	@GetMapping(path = "/ping", produces = MediaType.TEXT_HTML_VALUE)
	public ResponseEntity<String> ping() throws Exception {
		return ResponseEntity.ok("<b>running... 1</b>");
	}

}
