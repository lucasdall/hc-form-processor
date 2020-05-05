package life.heartcare.formprocessor.service.comorbidities;

import org.springframework.stereotype.Component;

import life.heartcare.formprocessor.dto.AnswerDTO;
import life.heartcare.formprocessor.dto.AnswerListDTO;
import life.heartcare.formprocessor.dto.enums.QuestionsLabelsId;

@Component
public class AgeGroupScore implements ScoreCalculator {

	@Override
	public Integer calc(AnswerListDTO answers) {
		AnswerDTO hcAge = answers.getById(QuestionsLabelsId.HC_BIRTHDAY);
		if (hcAge != null) {
			Long age = hcAge.getNumber();
			if (age >= 60 && age <= 80) {
				return 50;
			} else if (age > 80) {
				return 80;
			}
		}
		return 0;
	}

}
