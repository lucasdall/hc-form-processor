package life.heartcare.formprocessor.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import life.heartcare.formprocessor.dto.AnswerListDTO;
import life.heartcare.formprocessor.dto.enums.Results;
import life.heartcare.formprocessor.service.rules.Type04AsymptomaticHighSuspicionRuleValidator;
import life.heartcare.formprocessor.service.rules.Type03AsymptomaticLowerSuspicionRuleValidator;
import life.heartcare.formprocessor.service.rules.Type10DiagnosedCuredRuleValidator;
import life.heartcare.formprocessor.service.rules.Type09DiagnosedNotCuredAsymptomaticRuleValidator;
import life.heartcare.formprocessor.service.rules.Type08DiagnosedNotCuredSymptomaticRuleValidator;
import life.heartcare.formprocessor.service.rules.Type07HasNothingJustCuriousRuleValidator;
import life.heartcare.formprocessor.service.rules.RuleValidator;
import life.heartcare.formprocessor.service.rules.Type06SymptomaticFluSuspicionRuleValidator;
import life.heartcare.formprocessor.service.rules.Type01SymptomaticHighSuspicionOfCovid19RuleValidator;
import life.heartcare.formprocessor.service.rules.Type02SymptomaticLowerSuspicionOfCovid19RuleValidator;
import life.heartcare.formprocessor.service.rules.Type05SymptomaticLowerSuspicionRuleValidator;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RulesService {

	@Autowired
	private Type01SymptomaticHighSuspicionOfCovid19RuleValidator type01;

	@Autowired
	private Type02SymptomaticLowerSuspicionOfCovid19RuleValidator type02;

	@Autowired
	private Type03AsymptomaticLowerSuspicionRuleValidator type03;

	@Autowired
	private Type04AsymptomaticHighSuspicionRuleValidator type04;

	@Autowired
	private Type05SymptomaticLowerSuspicionRuleValidator type05;

	@Autowired
	private Type06SymptomaticFluSuspicionRuleValidator type06;

	@Autowired
	private Type07HasNothingJustCuriousRuleValidator type07;

	@Autowired
	private Type08DiagnosedNotCuredSymptomaticRuleValidator type08;

	@Autowired
	private Type09DiagnosedNotCuredAsymptomaticRuleValidator type09;

	@Autowired
	private Type10DiagnosedCuredRuleValidator type10;

	private List<RuleValidator> rules = new ArrayList<>(0);

	@PostConstruct
	public void setup() {
		rules.addAll(Arrays.asList(type01, type02, type03, type04, type05, type06, type07, type08, type09, type10));
	}

	public Results execute(AnswerListDTO answers) {
		log.info("begin - execute");
		Results res = Results.TYPE_11_Unknown;
		try {
			for (RuleValidator rule : rules) {
				log.info("rule type [{}]", rule.getResult());
				Boolean returnBool = rule.match(answers);
				if (Boolean.TRUE.equals(returnBool)) {
					res = rule.getResult();
					break;
				}
			}

		} finally {
			log.info("end - execute");
		}
		log.info("result [{}]", res);
		return res;
	}

}
