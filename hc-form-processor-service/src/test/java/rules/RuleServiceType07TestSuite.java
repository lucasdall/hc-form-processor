package rules;

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
import life.heartcare.formprocessor.dto.enums.Results;
import life.heartcare.formprocessor.service.RulesService;
import life.heartcare.formprocessor.service.cfg.JacksonCfg;

@RunWith(SpringRunner.class)
@Import({RulesService.class, JacksonCfg.class})
@ComponentScan("life.heartcare.formprocessor.service.rules")
public class RuleServiceType07TestSuite {

	@Autowired
	private RulesService rulesService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Test
	public void type01_07_id147() throws Exception {
		assertTrue(Results.TYPE_07_HasNothingJustCurious.equals(runType("/rules/type_07_id147.txt")));
	}

	@Test
	public void type01_07_id148() throws Exception {
		assertTrue(Results.TYPE_07_HasNothingJustCurious.equals(runType("/rules/type_07_id148.txt")));
	}

	@Test
	public void type01_07_id149() throws Exception {
		assertTrue(Results.TYPE_07_HasNothingJustCurious.equals(runType("/rules/type_07_id149.txt")));
	}

	@Test
	public void type01_07_id150() throws Exception {
		assertTrue(Results.TYPE_07_HasNothingJustCurious.equals(runType("/rules/type_07_id150.txt")));
	}

	@Test
	public void type01_07_id158() throws Exception {
		assertTrue(Results.TYPE_07_HasNothingJustCurious.equals(runType("/rules/type_07_id158.txt")));
	}

	private Results runType(String file) throws Exception {
		String json  = IOUtils.toString(RuleServiceType07TestSuite.class.getResourceAsStream(file), StandardCharsets.UTF_8);
		AnswerListDTO a = loadPayload(json);
		return rulesService.execute(a);
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
