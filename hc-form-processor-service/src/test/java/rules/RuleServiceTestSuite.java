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
public class RuleServiceTestSuite {

	@Autowired
	private RulesService rulesService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Test
	public void type01_001() throws Exception {
		assertTrue(Results.TYPE_01_SymptomaticHighSuspicionOfCovid19.equals(runType("/rules/type_01_001.txt")));
	}

	@Test
	public void type01_005() throws Exception {
		assertTrue(Results.TYPE_01_SymptomaticHighSuspicionOfCovid19.equals(runType("/rules/type_01_005.txt")));
	}

	@Test
	public void type01_005_rota5() throws Exception {
		assertTrue(Results.TYPE_01_SymptomaticHighSuspicionOfCovid19.equals(runType("type_001_rota5.txt")));
	}

	@Test
	public void type01_rota7() throws Exception {
		assertTrue(Results.TYPE_01_SymptomaticHighSuspicionOfCovid19.equals(runType("type_01_rota7.txt")));
	}

	@Test
	public void type01_rota8() throws Exception {
		assertTrue(Results.TYPE_01_SymptomaticHighSuspicionOfCovid19.equals(runType("type_01_rota8.txt")));
	}

	@Test
	public void type01_007() throws Exception {
		assertTrue(Results.TYPE_01_SymptomaticHighSuspicionOfCovid19.equals(runType("/rules/type_01_007.txt")));
	}

	@Test
	public void type03_001() throws Exception {
		assertTrue(Results.TYPE_03_AsymptomaticLowerSuspicion.equals(runType("/rules/type_03_001.txt")));
	}


	@Test
	public void type03_002() throws Exception {
		assertTrue(Results.TYPE_03_AsymptomaticLowerSuspicion.equals(runType("/rules/type_03_002.txt")));
	}

	@Test
	public void type03_003() throws Exception {
		assertTrue(Results.TYPE_03_AsymptomaticLowerSuspicion.equals(runType("/rules/type_03_003.txt")));
	}

	@Test
	public void type03_004() throws Exception {
		assertTrue(Results.TYPE_03_AsymptomaticLowerSuspicion.equals(runType("/rules/type_03_004.txt")));
	}

	@Test
	public void type04_001() throws Exception {
		assertTrue(Results.TYPE_04_AsymptomaticHighSuspicion.equals(runType("/rules/type_04_001.txt")));
	}

	@Test
	public void type10_001() throws Exception {
		assertTrue(Results.TYPE_10_DiagnosedCured.equals(runType("/rules/type_10_001.txt")));
	}

	@Test
	public void type10_002() throws Exception {
		assertTrue(Results.TYPE_10_DiagnosedCured.equals(runType("/rules/type_10_002.txt")));
	}

	
	@Test
	public void type06_001() throws Exception {
		assertTrue(Results.TYPE_06_SymptomaticFluSuspicion.equals(runType("/rules/type_06_001.txt")));
	}

	@Test
	public void type06_002() throws Exception {
		assertTrue(Results.TYPE_06_SymptomaticFluSuspicion.equals(runType("/rules/type_06_002.txt")));
	}

	@Test
	public void type09_001() throws Exception {
		assertTrue(Results.TYPE_09_DiagnosedNotCuredAsymptomatic.equals(runType("/rules/type_09_001.txt")));
	}

	private Results runType(String file) throws Exception {
		String json  = IOUtils.toString(RuleServiceTestSuite.class.getResourceAsStream(file), StandardCharsets.UTF_8);
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
