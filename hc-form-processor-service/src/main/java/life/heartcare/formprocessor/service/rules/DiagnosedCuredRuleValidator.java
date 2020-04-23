package life.heartcare.formprocessor.service.rules;

import org.springframework.stereotype.Component;

import life.heartcare.formprocessor.dto.AnswerDTO;
import life.heartcare.formprocessor.dto.AnswerListDTO;
import life.heartcare.formprocessor.dto.enums.QuestionsLabelsId;
import life.heartcare.formprocessor.dto.enums.Results;

@Component
public class DiagnosedCuredRuleValidator implements RuleValidator {

	@Override
	public boolean match(AnswerListDTO answers) {
		AnswerDTO hcTest = answers.getById(QuestionsLabelsId.HC_TEST);
		AnswerDTO hcCovidRecovered = answers.getById(QuestionsLabelsId.HC_COVID_RECOVERED);
		AnswerDTO hcSymptomsType = answers.getById(QuestionsLabelsId.HC_SYMPTOMS_TYPE);

		if (hcTest != null) {
			Boolean hcTestCond = hcTest.getChoice().testAny("fiz o teste e tenho o resultado de covid-19 positivo", "fiz o teste e já estou imune ao vírus");
			if (hcTestCond && hcCovidRecovered != null) {
				Boolean hcCovidRecoveredCond = Boolean.TRUE.equals(hcCovidRecovered.getBooleanVal());
				if (hcCovidRecoveredCond) {
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public Results getResult() {
		return Results.TYPE_10_DiagnosedCured;
	}

}
