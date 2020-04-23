package life.heartcare.formprocessor.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import life.heartcare.formprocessor.dto.AnswerListDTO;
import life.heartcare.formprocessor.dto.enums.Results;
import life.heartcare.formprocessor.service.rules.AsymptomaticHighSuspicionRuleValidator;
import life.heartcare.formprocessor.service.rules.AsymptomaticLowerSuspicionRuleValidator;
import life.heartcare.formprocessor.service.rules.DiagnosedCuredRuleValidator;
import life.heartcare.formprocessor.service.rules.DiagnosedNotCuredAsymptomaticRuleValidator;
import life.heartcare.formprocessor.service.rules.DiagnosedNotCuredSymptomaticRuleValidator;
import life.heartcare.formprocessor.service.rules.HasNothingJustCuriousRuleValidator;
import life.heartcare.formprocessor.service.rules.RuleValidator;
import life.heartcare.formprocessor.service.rules.SymptomaticFluSuspicionRuleValidator;
import life.heartcare.formprocessor.service.rules.SymptomaticHighSuspicionOfCovid19RuleValidator;
import life.heartcare.formprocessor.service.rules.SymptomaticLowerSuspicionOfCovid19RuleValidator;
import life.heartcare.formprocessor.service.rules.SymptomaticLowerSuspicionRuleValidator;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RulesService {

	@Autowired
	private SymptomaticHighSuspicionOfCovid19RuleValidator type01;

	@Autowired
	private SymptomaticLowerSuspicionOfCovid19RuleValidator type02;

	@Autowired
	private AsymptomaticLowerSuspicionRuleValidator type03;

	@Autowired
	private AsymptomaticHighSuspicionRuleValidator type04;

	@Autowired
	private SymptomaticLowerSuspicionRuleValidator type05;

	@Autowired
	private SymptomaticFluSuspicionRuleValidator type06;

	@Autowired
	private HasNothingJustCuriousRuleValidator type07;

	@Autowired
	private DiagnosedNotCuredSymptomaticRuleValidator type08;

	@Autowired
	private DiagnosedNotCuredAsymptomaticRuleValidator type09;

	@Autowired
	private DiagnosedCuredRuleValidator type10;

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
				log.info("rule type [{}]", rule.getClass().getSimpleName());
				Boolean returnBool = rule.match(answers);
				if (Boolean.TRUE.equals(returnBool)) {
					res = rule.getResult();
				}
			}

		} finally {
			log.info("end - execute");
		}
		log.info("result [{}]", res);
		return res;
	}

}
