package comorbidities;

import static org.junit.Assert.assertTrue;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import life.heartcare.formprocessor.dto.AnswerDTO;
import life.heartcare.formprocessor.dto.AnswerListDTO;
import life.heartcare.formprocessor.dto.enums.ComorbiditiesScore;
import life.heartcare.formprocessor.service.ComorbiditiesService;
import life.heartcare.formprocessor.service.cfg.JacksonCfg;

@RunWith(SpringRunner.class)
@Import({ComorbiditiesService.class, JacksonCfg.class})
@ComponentScan("life.heartcare.formprocessor.service.comorbidities")
public class ComorbiditiesTestSuite {

	@Autowired
	private ComorbiditiesService comorbiditiesService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Test
	public void type01_HIGHT() throws Exception {
		assertTrue(ComorbiditiesScore.HIGHT.equals(runType("/rules/type_01_template.json")));
	}

	private ComorbiditiesScore runType(String file) throws Exception {
		String json  = IOUtils.toString(ComorbiditiesTestSuite.class.getResourceAsStream(file), StandardCharsets.UTF_8);
		AnswerListDTO a = loadPayload(json);
		return comorbiditiesService.execute(a);
	}
	
	@SuppressWarnings("unchecked")
	private AnswerListDTO loadPayload(String json) throws Exception {
		json = json.replaceAll("\\\\\"", "\"");
		Map<String, Object> payloadMap = objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
		Map<String, Object> formResponse = (Map<String, Object>) payloadMap.get("form_response");
		if (formResponse != null) {
			List<Map<String, Object>> answersList = (List<Map<String, Object>>) formResponse.get("answers");
			return new AnswerListDTO(objectMapper.convertValue(answersList, new TypeReference<List<AnswerDTO>>() {}));
		}
		return null;
	}
	
}
