package life.heartcare.formprocessor.service.rules;

import org.springframework.stereotype.Component;

import life.heartcare.formprocessor.dto.AnswerDTO;
import life.heartcare.formprocessor.dto.AnswerListDTO;
import life.heartcare.formprocessor.dto.enums.QuestionsLabelsId;
import life.heartcare.formprocessor.dto.enums.Results;

@Component
public class Type02SymptomaticLowerSuspicionOfCovid19RuleValidator implements RuleValidator {

	@Override
	public boolean match(AnswerListDTO answers) {
		AnswerDTO hcTest = answers.getById(QuestionsLabelsId.HC_TEST);
		AnswerDTO hcSymptomsType = answers.getById(QuestionsLabelsId.HC_SYMPTOMS_TYPE);
		AnswerDTO hcSymptomsCritical = answers.getById(QuestionsLabelsId.HC_SYMPTOMS_CRITICAL);
		AnswerDTO hcSymptomsOthers = answers.getById(QuestionsLabelsId.HC_SYMPTOMS_OTHERS);

		// RULE 01
		if (hcTest != null) {
			Boolean hcTestCond = hcTest.getChoice()
					.testAny("fiz o teste e tenho o resultado de covid-19 negativo",
						  "fiz o teste, mas ainda estou aguardando o resultado",
						  "quero fazer o teste",
						  "não quero fazer o teste");
			if (hcTestCond && hcSymptomsType != null) {
				Boolean hcSymptomsTypeCond = 
						hcSymptomsType.getChoices().getLabels().size() >= 2 
						|| hcSymptomsType.getChoices().testAny("falta de ar")
						&& hcSymptomsType.getChoices().testAny("nenhum destes") == false;
				if (hcSymptomsTypeCond && hcSymptomsCritical != null) {
					Boolean hcSymptomsCriticalCond = Boolean.FALSE.equals(hcSymptomsCritical.getBooleanVal());
					if (hcSymptomsCriticalCond) {
						Boolean hcSymptomsOthersCond = 
								hcSymptomsOthers.getChoices().getLabels().isEmpty() == false
								&& hcSymptomsOthers.getChoices().testAny("falta de olfato","falta de paladar") == false;
						if (hcSymptomsOthersCond) {
								return true;
						}
					}
				}
			}
		}
		
		// RULE 02
		if (hcTest != null) {
			Boolean hcTestCond = hcTest.getChoice()
					.testAny("fiz o teste e tenho o resultado de covid-19 negativo",
						  "fiz o teste, mas ainda estou aguardando o resultado",
						  "quero fazer o teste",
						  "não quero fazer o teste");
			if (hcTestCond) {
				Boolean hcSymptomsOthersCond = hcSymptomsOthers.getChoices().testAny("falta de olfato","falta de paladar");
				if (hcSymptomsOthersCond) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	@Override
	public Results getResult() {
		return Results.TYPE_02_SymptomaticLowerSuspicionOfCovid19;
	}
}
