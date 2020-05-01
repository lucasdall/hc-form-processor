package life.heartcare.formprocessor.service.rules;

import org.springframework.stereotype.Component;

import life.heartcare.formprocessor.dto.AnswerDTO;
import life.heartcare.formprocessor.dto.AnswerListDTO;
import life.heartcare.formprocessor.dto.enums.QuestionsLabelsId;
import life.heartcare.formprocessor.dto.enums.Results;

@Component
public class Type06SymptomaticFluSuspicionRuleValidator implements RuleValidator {

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
			if (hcSymptomsType != null && hcTestCond) {
				Boolean hcSymptomsTypeCond = hcSymptomsType.getChoices().testAny("tosse", "dor de garganta", "febre");
				if (hcSymptomsTypeCond && hcSymptomsCritical != null) {
					Boolean hcSymptomsCriticalCond = Boolean.FALSE.equals(hcSymptomsCritical.getBooleanVal());
					if (hcSymptomsCriticalCond && hcSymptomsOthers != null) {
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
			if (hcSymptomsType != null && hcTestCond) {
				Boolean hcSymptomsTypeCond = hcSymptomsType.getChoices().testAny("nenhum destes");
				if (hcSymptomsTypeCond && hcSymptomsOthers != null) {
					Boolean hcSymptomsOthersCond = 
							hcSymptomsOthers.getChoices().getLabels().isEmpty() == false
							&& hcSymptomsOthers.getChoices().testAny("falta de olfato", "falta de paladar", "nenhum destes") == false;
					if (hcSymptomsOthersCond) {
						return true;
					}
				}
			}
		}

		// RULE 03
		if (hcTest != null) {
			Boolean hcTestCond = hcTest.getChoice()
					.testAny("fiz o teste e tenho o resultado de covid-19 negativo",
						  "fiz o teste, mas ainda estou aguardando o resultado",
						  "quero fazer o teste",
						  "não quero fazer o teste");
			if (hcSymptomsType != null && hcTestCond) {
				Boolean hcSymptomsTypeCond = hcSymptomsType.getChoices().testAny("nenhum destes", "falta de ar") == false && hcSymptomsType.getChoices().getLabels().size() >= 1;
				if (hcSymptomsTypeCond && hcSymptomsCritical != null) {
					Boolean hcSymptomsCriticalCond = Boolean.TRUE.equals(hcSymptomsCritical.getBooleanVal());
					if (hcSymptomsCriticalCond && hcSymptomsOthers != null) {
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

		return false;
	}
	
	@Override
	public Results getResult() {
		return Results.TYPE_06_SymptomaticFluSuspicion;
	}


}
