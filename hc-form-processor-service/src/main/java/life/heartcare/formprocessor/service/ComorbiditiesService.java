package life.heartcare.formprocessor.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import life.heartcare.formprocessor.dto.AnswerListDTO;
import life.heartcare.formprocessor.dto.enums.ComorbiditiesScore;
import life.heartcare.formprocessor.service.comorbidities.AgeGroupScore;
import life.heartcare.formprocessor.service.comorbidities.ImcScore;
import life.heartcare.formprocessor.service.comorbidities.ScoreCalculator;
import life.heartcare.formprocessor.service.comorbidities.SmokeScore;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ComorbiditiesService {

	@Autowired
	private AgeGroupScore ageGroupScore;
	
	@Autowired
	private life.heartcare.formprocessor.service.comorbidities.ComorbiditiesScore comorbiditiesScore;
	
	@Autowired
	private SmokeScore smokeScore;

	@Autowired
	private ImcScore imsScore;
	
	private List<ScoreCalculator> scores = new ArrayList<>(0);

	@PostConstruct
	public void setup() {
		scores.addAll(Arrays.asList(ageGroupScore, comorbiditiesScore, smokeScore, imsScore));
	}

	public ComorbiditiesScore execute(AnswerListDTO answers) {
		log.info("begin - execute");
		ComorbiditiesScore comormb = ComorbiditiesScore.LOW;
		Integer totalScore = 0;
		try {
			for (ScoreCalculator score : scores) {
				Integer scoreResp = score.calc(answers);
				log.info("score type [{}] - result [{}]", score.getClass().getSimpleName(), scoreResp);
				totalScore = totalScore + scoreResp;
			}
			if (totalScore >= 100) {
				comormb = ComorbiditiesScore.HIGHT;
			} else if (totalScore >= 70 && totalScore <= 99) {
				comormb = ComorbiditiesScore.MEDIUM_HIGHT;
			} else if (totalScore >= 50 && totalScore <= 69) {
				comormb = ComorbiditiesScore.MEDIUM;
			} else if (totalScore >= 30 && totalScore <= 49) {
				comormb = ComorbiditiesScore.MEDIUM_LOW;
			} else if (totalScore <= 29) {
				comormb = ComorbiditiesScore.LOW;
			}
			log.info("ComorbiditiesScore [{}] - total score [{}]", comormb, totalScore);
		} finally {
			log.info("end - execute");
		}
		log.info("comorbidities [{}]", comormb);
		return comormb;
	}

}
