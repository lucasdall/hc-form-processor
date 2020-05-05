package life.heartcare.formprocessor.service.comorbidities;

import life.heartcare.formprocessor.dto.AnswerListDTO;

public interface ScoreCalculator {

	Integer calc(AnswerListDTO answers);
	
}
