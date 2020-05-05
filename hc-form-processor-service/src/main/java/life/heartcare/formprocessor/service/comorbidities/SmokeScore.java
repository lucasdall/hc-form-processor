package life.heartcare.formprocessor.service.comorbidities;

import org.springframework.stereotype.Component;

import life.heartcare.formprocessor.dto.AnswerDTO;
import life.heartcare.formprocessor.dto.AnswerListDTO;
import life.heartcare.formprocessor.dto.enums.QuestionsLabelsId;

@Component
public class SmokeScore implements ScoreCalculator {

	@Override
	public Integer calc(AnswerListDTO answers) {
		AnswerDTO hcSmoke = answers.getById(QuestionsLabelsId.HC_SMOKE);
		Integer score = 0;
		if (hcSmoke != null) {
			if (hcSmoke.getChoice().getLabel().equalsIgnoreCase("Fumo diariamente")) {
				score = score + 40;
			}
			if (hcSmoke.getChoice().getLabel().equalsIgnoreCase("Fumei no passado recente")) {
				score = score + 20;
			}
			if (hcSmoke.getChoice().getLabel().equalsIgnoreCase("Parei de fumar há mais de 5 anos")) {
				score = score + 0;
			}
			if (hcSmoke.getChoice().getLabel().equalsIgnoreCase("Nunca fumei")) {
				score = score + 0;
			}
		}
		
		return score;
	}

}
