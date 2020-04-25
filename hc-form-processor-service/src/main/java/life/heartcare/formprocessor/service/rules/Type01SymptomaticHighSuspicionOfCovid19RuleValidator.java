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
		AnswerDTO hcSymptomsBreathe = answers.getById(QuestionsLabelsId.HC_SYMPTOMS_BREATHE);

		// RULE 1
		if (hcTest != null) {
			Boolean hcTestCond = hcTest.getChoice()
					.testAny("fiz o teste e tenho o resultado de covid-19 negativo",
						  "fiz o teste, mas ainda estou aguardando o resultado",
						  "quero fazer o teste",
						  "não quero fazer o teste");
			if (hcSymptomsType != null && hcTestCond) {
				Boolean hcSymptomsTypeCond = hcSymptomsType.getChoices().testAny("falta de ar");
				hcSymptomsTypeCond = hcSymptomsTypeCond && hcSymptomsType.getChoices().testAny("nenhum destes") == false;
				if (hcSymptomsCritical != null && hcSymptomsTypeCond) {
					Boolean hcSymptomsCriticalCond = Boolean.TRUE.equals(hcSymptomsCritical.getBooleanVal());
					if (hcSymptomsCriticalCond) {
						return true;
					}
				}
			}
		}
		// or - RULE 2
		if (hcTest != null) {
			Boolean hcTestCond = hcTest.getChoice()
					.testAny("fiz o teste e tenho o resultado de covid-19 negativo",
						  "fiz o teste, mas ainda estou aguardando o resultado",
						  "quero fazer o teste",
						  "não quero fazer o teste");
			if (hcSymptomsType != null && hcTestCond) {
				Boolean hcSymptomsTypeCond = 
						hcSymptomsType.getChoices().testAny("falta de ar") 
						&& hcSymptomsType.getChoices().getLabels().size() > 1 
						&& hcSymptomsType.getChoices().testAny("nenhum destes") == false;
				if (hcSymptomsCritical != null && hcSymptomsTypeCond) {
					Boolean hcSymptomsCriticalCond = Boolean.FALSE.equals(hcSymptomsCritical.getBooleanVal());
					if (hcSymptomsCriticalCond) {
						return true;
					}
				}
			}
		}
		
		// or - RULE 3
		if (hcTest != null) {
			Boolean hcTestCond = hcTest.getChoice()
					.testAny("fiz o teste e tenho o resultado de covid-19 negativo",
						  "fiz o teste, mas ainda estou aguardando o resultado",
						  "quero fazer o teste",
						  "não quero fazer o teste");
			if (hcSymptomsType != null && hcTestCond) {
				Boolean hcSymptomsTypeCond = 
						hcSymptomsType.getChoices().getLabels().size() >= 3
						&& hcSymptomsType.getChoices().testAny("nenhum destes") == false;
				if (hcSymptomsTypeCond && hcSymptomsCritical != null) {
					Boolean hcSymptomsCriticalCond = Boolean.TRUE.equals(hcSymptomsCritical.getBooleanVal());
					if (hcSymptomsCriticalCond) {
						return true;
					}
				}
			}
		}
		
		// or - RULE 4
		if (hcTest != null) {
			Boolean hcTestCond = hcTest.getChoice()
					.testAny("fiz o teste e tenho o resultado de covid-19 negativo",
						  "fiz o teste, mas ainda estou aguardando o resultado",
						  "quero fazer o teste",
						  "não quero fazer o teste");
			if (hcSymptomsType != null && hcTestCond) {
				Boolean hcSymptomsTypeCond = 
						hcSymptomsType.getChoices().getLabels().size() > 2 
						&& hcSymptomsType.getChoices().testAny("nenhum destes") == false;
				if (hcSymptomsCritical != null && hcSymptomsTypeCond) {
					Boolean hcSymptomsCriticalCond = Boolean.TRUE.equals(hcSymptomsCritical.getBooleanVal());
					if (hcSymptomsCriticalCond) {
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
			
		}		
		
		// or - RULE 5
		if (hcTest != null) {
			Boolean hcTestCond = hcTest.getChoice()
					.testAny("fiz o teste e tenho o resultado de covid-19 negativo",
						  "fiz o teste, mas ainda estou aguardando o resultado",
						  "quero fazer o teste",
						  "não quero fazer o teste");
			if (hcSymptomsType != null && hcTestCond) {
				Boolean hcSymptomsTypeCond = 
						hcSymptomsType.getChoices().getLabels().size() >= 2 
						&& hcSymptomsType.getChoices().testAny("nenhum destes") == false;
						if (hcSymptomsTypeCond && hcSymptomsBreathe != null) {
							if (hcSymptomsOthers != null) {
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
