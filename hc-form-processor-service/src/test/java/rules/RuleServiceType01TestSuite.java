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
public class RuleServiceType01TestSuite {

	@Autowired
	private RulesService rulesService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Test
	public void type01_01_id89() throws Exception {
		assertTrue(Results.TYPE_01_SymptomaticHighSuspicionOfCovid19.equals(runType("/rules/type_01_id89.txt")));
	}

	@Test
	public void type01_005() throws Exception {
		assertTrue(Results.TYPE_01_SymptomaticHighSuspicionOfCovid19.equals(runType("/rules/type_01_005.txt")));
	}

	@Test
	public void type01_005_rota5() throws Exception {
		assertTrue(Results.TYPE_01_SymptomaticHighSuspicionOfCovid19.equals(runType("/rules/type_001_rota5.txt")));
	}

	@Test
	public void type01_rota7() throws Exception {
		assertTrue(Results.TYPE_01_SymptomaticHighSuspicionOfCovid19.equals(runType("/rules/type_01_rota7.txt")));
	}

	@Test
	public void type01_rota8() throws Exception {
		assertTrue(Results.TYPE_01_SymptomaticHighSuspicionOfCovid19.equals(runType("/rules/type_01_rota8.txt")));
	}

	@Test
	public void type01_007() throws Exception {
		assertTrue(Results.TYPE_01_SymptomaticHighSuspicionOfCovid19.equals(runType("/rules/type_01_007.txt")));
	}

	private Results runType(String file) throws Exception {
		String json  = IOUtils.toString(RuleServiceType01TestSuite.class.getResourceAsStream(file), StandardCharsets.UTF_8);
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
