package life.heartcare.formprocessor.service.rules;

import org.springframework.stereotype.Component;

import life.heartcare.formprocessor.dto.AnswerDTO;
import life.heartcare.formprocessor.dto.AnswerListDTO;
import life.heartcare.formprocessor.dto.enums.QuestionsLabelsId;
import life.heartcare.formprocessor.dto.enums.Results;

@Component
public class DiagnosedNotCuredSymptomaticRuleValidator implements RuleValidator {

	@Override
	public boolean match(AnswerListDTO answers) {
		AnswerDTO hcTest = answers.getById(QuestionsLabelsId.HC_TEST);
		AnswerDTO hcCovidRecovered = answers.getById(QuestionsLabelsId.HC_COVID_RECOVERED);
		AnswerDTO hcSymptomsType = answers.getById(QuestionsLabelsId.HC_SYMPTOMS_TYPE);
		AnswerDTO hcSymptomsOthers = answers.getById(QuestionsLabelsId.HC_SYMPTOMS_OTHERS);

		if (hcTest != null) {
			Boolean hcTestCond = hcTest.getChoice().testAny("fiz o teste e tenho o resultado de covid-19 positivo");
			if (hcTestCond && hcCovidRecovered != null) {
				Boolean hcCovidRecoveredCond = Boolean.FALSE.equals(hcSymptomsType.getBooleanVal());
				if (hcCovidRecoveredCond && hcSymptomsType != null) {
					Boolean hcSymptomsTypeCond = 
							hcSymptomsType.getChoices().testAll("tosse","febre") 
						 || hcSymptomsType.getChoices().testAll("tosse","dor de garganta")
						 || hcSymptomsType.getChoices().testAll("febre","dor de garganta")
						 || hcSymptomsType.getChoices().testAny("falta de ar");
					hcSymptomsTypeCond = hcSymptomsTypeCond && hcSymptomsType.getChoices().testAny("nenhum destes") == false;
					if (hcSymptomsTypeCond) {
						return true;
					}
				}
			}
		}
		// or
		if (hcTest != null) {
			Boolean hcTestCond = hcTest.getChoice().testAny("fiz o teste e tenho o resultado de covid-19 positivo");
			if (hcTestCond && hcCovidRecovered != null) {
				Boolean hcCovidRecoveredCond = Boolean.FALSE.equals(hcSymptomsType.getBooleanVal());
				if (hcCovidRecoveredCond && hcSymptomsType != null) {
					Boolean hcSymptomsTypeCond = hcSymptomsType.getChoices().getLabels().size() > 2;
					hcSymptomsTypeCond = hcSymptomsTypeCond && hcSymptomsType.getChoices().testAny("nenhum destes") == false;
					if (hcSymptomsTypeCond) {
						return true;
					}
				}
			}
		}
		// or
		if (hcTest != null) {
			Boolean hcTestCond = hcTest.getChoice().testAny("fiz o teste e tenho o resultado de covid-19 positivo");
			if (hcTestCond && hcCovidRecovered != null) {
				Boolean hcCovidRecoveredCond = Boolean.FALSE.equals(hcSymptomsType.getBooleanVal());
				if (hcCovidRecoveredCond && hcSymptomsType != null) {
					Boolean hcSymptomsTypeCond = 
							hcSymptomsType.getChoices().testAll("tosse","febre") 
						 || hcSymptomsType.getChoices().testAll("tosse","dor de garganta")
						 || hcSymptomsType.getChoices().testAll("febre","dor de garganta")
						 || hcSymptomsType.getChoices().testAny("falta de ar");
					hcSymptomsTypeCond = hcSymptomsTypeCond && hcSymptomsType.getChoices().testAny("nenhum destes") == false;
					if (hcSymptomsTypeCond && hcSymptomsOthers != null) {
						Boolean hcSymptomsOthersCond = !hcSymptomsOthers.getChoices().getLabels().isEmpty();
						hcSymptomsOthersCond = hcSymptomsOthersCond && hcSymptomsOthers.getChoices().testAny("falta de olfato", "falta de paladar") == false;
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
		return Results.TYPE_08_DiagnosedNotCuredSymptomatic;
	}

}
