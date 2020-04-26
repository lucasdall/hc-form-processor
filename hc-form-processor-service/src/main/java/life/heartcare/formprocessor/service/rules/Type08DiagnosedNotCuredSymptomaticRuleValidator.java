package life.heartcare.formprocessor.service.rules;

import org.springframework.stereotype.Component;

import life.heartcare.formprocessor.dto.AnswerDTO;
import life.heartcare.formprocessor.dto.AnswerListDTO;
import life.heartcare.formprocessor.dto.enums.QuestionsLabelsId;
import life.heartcare.formprocessor.dto.enums.Results;

@Component
public class Type08DiagnosedNotCuredSymptomaticRuleValidator implements RuleValidator {

	@Override
	public boolean match(AnswerListDTO answers) {
		AnswerDTO hcTest = answers.getById(QuestionsLabelsId.HC_TEST);
		AnswerDTO hcCovidRecovered = answers.getById(QuestionsLabelsId.HC_COVID_RECOVERED);
		AnswerDTO hcSymptomsType = answers.getById(QuestionsLabelsId.HC_SYMPTOMS_TYPE);
		AnswerDTO hcSymptomsOthers = answers.getById(QuestionsLabelsId.HC_SYMPTOMS_OTHERS);

		if (hcTest != null) {
			Boolean hcTestCond = hcTest.getChoice().testAny("Fiz o teste e tenho o resultado de COVID-19 positivo");
			if (hcTestCond && hcCovidRecovered != null) {
				Boolean hcCovidRecoveredCond = Boolean.FALSE.equals(hcCovidRecovered.getBooleanVal());
				Boolean hcSymptomsTypeCond = Boolean.FALSE; 
				if (hcSymptomsType != null) {
					hcSymptomsTypeCond = 
							hcSymptomsType.getChoices().getLabels().isEmpty() == false 
							&& hcSymptomsType.getChoices().testAny("nenhum destes") == false;
				}
				Boolean hcSymptomsOthersCond = Boolean.FALSE;
				if (hcSymptomsOthers != null) {
						hcSymptomsOthersCond =
							hcSymptomsOthers.getChoices().getLabels().isEmpty() == false
							&& hcSymptomsOthers.getChoices().testAny("nenhum destes") == false;
				}
				if (hcCovidRecoveredCond && (hcSymptomsTypeCond || hcSymptomsOthersCond)) {
					return true;
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
