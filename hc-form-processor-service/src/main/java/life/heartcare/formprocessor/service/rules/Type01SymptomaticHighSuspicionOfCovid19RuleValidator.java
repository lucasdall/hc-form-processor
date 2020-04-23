package life.heartcare.formprocessor.service.rules;

import org.springframework.stereotype.Component;

import life.heartcare.formprocessor.dto.AnswerDTO;
import life.heartcare.formprocessor.dto.AnswerListDTO;
import life.heartcare.formprocessor.dto.enums.QuestionsLabelsId;
import life.heartcare.formprocessor.dto.enums.Results;

@Component
public class Type01SymptomaticHighSuspicionOfCovid19RuleValidator implements RuleValidator {

	@Override
	public boolean match(AnswerListDTO answers) {
		AnswerDTO hcTest = answers.getById(QuestionsLabelsId.HC_TEST);
		AnswerDTO hcSymptomsType = answers.getById(QuestionsLabelsId.HC_SYMPTOMS_TYPE);
		AnswerDTO hcSymptomsCritical = answers.getById(QuestionsLabelsId.HC_SYMPTOMS_CRITICAL);
		AnswerDTO hcSymptomsOthers = answers.getById(QuestionsLabelsId.HC_SYMPTOMS_OTHERS);
		if (hcTest != null) {
			Boolean hcTestCond = hcTest.getChoice()
					.testAny("fiz o teste e tenho o resultado de covid-19 negativo",
						  "fiz o teste, mas ainda estou aguardando o resultado",
						  "quero fazer o teste",
						  "não quero fazer o teste");
			if (hcSymptomsType != null && hcTestCond) {
				Boolean hcSymptomsTypeCond = 
						hcSymptomsType.getChoices().testAll("tosse","febre") 
					 || hcSymptomsType.getChoices().testAll("tosse","dor de garganta")
					 || hcSymptomsType.getChoices().testAll("febre","dor de garganta")
					 || hcSymptomsType.getChoices().testAny("falta de ar");
				hcSymptomsTypeCond = hcSymptomsTypeCond && hcSymptomsType.getChoices().testAny("nenhum destes") == false;
				if (hcSymptomsCritical != null && hcSymptomsTypeCond) {
					Boolean hcSymptomsCriticalCond = Boolean.TRUE.equals(hcSymptomsCritical.getBooleanVal());
					if (hcSymptomsCriticalCond) {
						return true;
					}
				}
			}
		}
		// or
		if (hcTest != null) {
			Boolean hcTestCond = hcTest.getChoice()
					.testAny("fiz o teste e tenho o resultado de covid-19 negativo",
						  "fiz o teste, mas ainda estou aguardando o resultado",
						  "não fiz e quero fazer",
						  "Não fiz e não quero fazer");
			if (hcSymptomsType != null && hcTestCond) {
				Boolean hcSymptomsTypeCond = hcSymptomsType.getChoices().getLabels().size() > 2;
				hcSymptomsTypeCond = hcSymptomsTypeCond && hcSymptomsType.getChoices().testAny("nenhum destes") == false;
				
				if (hcSymptomsCritical != null && hcSymptomsTypeCond) {
					Boolean hcSymptomsCriticalCond = Boolean.FALSE.equals(hcSymptomsCritical.getBooleanVal());
					if (hcSymptomsCriticalCond) {
						return true;
					}
				}
			}
		}
		
		// or
		if (hcTest != null) {
			Boolean hcTestCond = hcTest.getChoice()
					.testAny("fiz o teste e tenho o resultado de covid-19 negativo",
						  "fiz o teste, mas ainda estou aguardando o resultado",
						  "não fiz e quero fazer",
						  "Não fiz e não quero fazer");
			if (hcSymptomsType != null && hcTestCond) {
				Boolean hcSymptomsTypeCond = 
						hcSymptomsType.getChoices().testAll("tosse","febre") 
					 || hcSymptomsType.getChoices().testAll("tosse","dor de garganta")
					 || hcSymptomsType.getChoices().testAll("febre","dor de garganta")
					 || hcSymptomsType.getChoices().testAny("falta de ar");
				hcSymptomsTypeCond = hcSymptomsTypeCond && hcSymptomsType.getChoices().testAny("nenhum destes") == false;
				if (hcSymptomsCritical != null && hcSymptomsTypeCond) {
					Boolean hcSymptomsCriticalCond = Boolean.TRUE.equals(hcSymptomsCritical.getBooleanVal());
					if (hcSymptomsCriticalCond && hcSymptomsOthers != null) {
						Boolean hcSymptomsOthersCond = hcSymptomsOthers.getChoices()
								.testAny("falta de olfato", "falta de paladar");
						if (hcSymptomsOthersCond) {
							return true;
						}
					}
				}
			}
		}
		
		return false;
	}
	
	@Override
	public Results getResult() {
		return Results.TYPE_01_SymptomaticHighSuspicionOfCovid19;
	}


}
