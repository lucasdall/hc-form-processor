package life.heartcare.formprocessor.service.rules;

import org.springframework.stereotype.Component;

import life.heartcare.formprocessor.dto.AnswerDTO;
import life.heartcare.formprocessor.dto.AnswerListDTO;
import life.heartcare.formprocessor.dto.enums.QuestionsLabelsId;
import life.heartcare.formprocessor.dto.enums.Results;

@Component
public class Type10DiagnosedCuredRuleValidator implements RuleValidator {

	@Override
	public boolean match(AnswerListDTO answers) {
		AnswerDTO hcTest = answers.getById(QuestionsLabelsId.HC_TEST);
		AnswerDTO hcCovidRecovered = answers.getById(QuestionsLabelsId.HC_COVID_RECOVERED);

		if (hcTest != null) {
			Boolean hcTestCond = hcTest.getChoice().testAny("Fiz o teste e tenho o resultado de COVID-19 positivo");
			if (hcTestCond && hcCovidRecovered != null) {
				Boolean hcCovidRecoveredCond = Boolean.TRUE.equals(hcCovidRecovered.getBooleanVal());
				if (hcCovidRecoveredCond) {
					return true;
				}
			}
		}
		// or
		if (hcTest != null) {
			Boolean hcTestCond = hcTest.getChoice().testAny("Fiz o teste e já estou imune ao vírus");
			if (hcTestCond) {
				return true;
			}
		}

		return false;
	}

	@Override
	public Results getResult() {
		return Results.TYPE_10_DiagnosedCured;
	}

}
