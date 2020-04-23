package life.heartcare.formprocessor.service.rules;

import org.springframework.stereotype.Component;

import life.heartcare.formprocessor.dto.AnswerDTO;
import life.heartcare.formprocessor.dto.AnswerListDTO;
import life.heartcare.formprocessor.dto.enums.QuestionsLabelsId;
import life.heartcare.formprocessor.dto.enums.Results;

@Component
public class DiagnosedNotCuredAsymptomaticRuleValidator implements RuleValidator {


	@Override
	public boolean match(AnswerListDTO answers) {
		AnswerDTO hcTest = answers.getById(QuestionsLabelsId.HC_TEST);
		AnswerDTO hcCovidRecovered = answers.getById(QuestionsLabelsId.HC_COVID_RECOVERED);
		AnswerDTO hcSymptomsType = answers.getById(QuestionsLabelsId.HC_SYMPTOMS_TYPE);
		AnswerDTO hcSymptomsBreathe = answers.getById(QuestionsLabelsId.HC_SYMPTOMS_BREATHE);
		AnswerDTO hcSymptomsOthers = answers.getById(QuestionsLabelsId.HC_SYMPTOMS_OTHERS);
		AnswerDTO hcContactInfected = answers.getById(QuestionsLabelsId.HC_CONTACT_INFECTED);

		if (hcTest != null) {
			Boolean hcTestCond = hcTest.getChoice().testAny("fiz o teste e tenho o resultado de covid-19 positivo");
			if (hcTestCond && hcCovidRecovered != null) {
				Boolean hcCovidRecoveredCond = Boolean.FALSE.equals(hcSymptomsType.getBooleanVal());
				if (hcCovidRecoveredCond && hcSymptomsType != null) {
					Boolean hcSymptomsTypeCond = hcSymptomsType.getChoices().testAny("nenhum destes");
					if (hcSymptomsTypeCond && hcSymptomsBreathe != null) {
						Boolean hcSymptomsBreatheCond = hcSymptomsBreathe.getChoices().testAny("está normal");
						if (hcSymptomsBreatheCond && hcSymptomsOthers != null) {
							Boolean hcSymptomsOthersCond = hcSymptomsOthers.getChoices().testAny("nenhum destes");
							if(hcSymptomsOthersCond && hcContactInfected != null) {
								Boolean hcContactInfectedCond = hcContactInfected.getChoices().testAny("nenhuma destas opções");
								if (hcContactInfectedCond) {
									return true;
								}
								
							}
						}
					}
				}
			}
		}
		
		return false;
	}
	
	@Override
	public Results getResult() {
		return Results.TYPE_09_DiagnosedNotCuredAsymptomatic;
	}
	

}
