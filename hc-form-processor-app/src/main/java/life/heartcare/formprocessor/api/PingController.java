package life.heartcare.formprocessor.api;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {

	private Properties res = new Properties();
	
	@PostConstruct
	public void setup() throws Exception {
		res.load(new ClassPathResource("/version.properties").getInputStream());
	}
	
	@GetMapping(path = "/ping", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Object>> ping() throws Exception {
		Map<String, Object> map = new HashMap<>();
		map.put("running", "true");
		map.put("version", res.getProperty("version"));
		map.put("build-date", DateFormatUtils.ISO_8601_EXTENDED_DATETIME_TIME_ZONE_FORMAT.parse((String)res.getProperty("build.date")));
		return ResponseEntity.ok(map);
	}

}
