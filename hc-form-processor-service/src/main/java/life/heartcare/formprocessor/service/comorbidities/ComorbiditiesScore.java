package life.heartcare.formprocessor.service.comorbidities;

import org.springframework.stereotype.Component;

import life.heartcare.formprocessor.dto.AnswerDTO;
import life.heartcare.formprocessor.dto.AnswerListDTO;
import life.heartcare.formprocessor.dto.enums.QuestionsLabelsId;

@Component
public class ComorbiditiesScore implements ScoreCalculator {

	@Override
	public Integer calc(AnswerListDTO answers) {
		AnswerDTO hcHealthHistory = answers.getById(QuestionsLabelsId.HC_HEALTH_HISTORY);
		Integer score = 0;
		if (hcHealthHistory != null) {
			if (hcHealthHistory.getChoices().testAny("Insuficiência cardíaca")) {
				score = score + 40;
			}
			if (hcHealthHistory.getChoices().testAny("Doença pulmonar em tratamento")) {
				score = score + 30;
			}
			if (hcHealthHistory.getChoices().testAny("Tenho Câncer")) {
				score = score + 20;
			}
			if (hcHealthHistory.getChoices().testAny("Me curei recentemente de um Câncer")) {
				score = score + 40;
			}
			if (hcHealthHistory.getChoices().testAny("Doença Renal")) {
				score = score + 40;
			}
			if (hcHealthHistory.getChoices().testAny("Hipertensão")) {
				score = score + 30;
			}
			if (hcHealthHistory.getChoices().testAny("Diabetes")) {
				score = score + 30;
			}
			if (hcHealthHistory.getChoices().testAny("Hepatite")) {
				score = score + 20;
			}
			if (hcHealthHistory.getChoices().testAny("HIV")) {
				score = score + 30;
			}
			if (hcHealthHistory.getChoices().testAny("Tuberculose")) {
				score = score + 30;
			}
			if (hcHealthHistory.getChoices().testAny("Asma")) {
				score = score + 30;
			}
			if (hcHealthHistory.getChoices().testAny("Autismo")) {
				score = score + 10;
			}
			if (hcHealthHistory.getChoices().testAny("Doença Rara")) {
				score = score + 10;
			}
		}
		
		return score;
	}

}
