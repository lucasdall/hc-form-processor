package life.heartcare.formprocessor.service.comorbidities;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import org.springframework.stereotype.Component;

import life.heartcare.formprocessor.dto.AnswerDTO;
import life.heartcare.formprocessor.dto.AnswerListDTO;
import life.heartcare.formprocessor.dto.enums.QuestionsLabelsId;

@Component
public class ImcScore implements ScoreCalculator {
	
	private MathContext mathCtx = new MathContext(4, RoundingMode.HALF_DOWN);

	@Override
	public Integer calc(AnswerListDTO answers) {
		AnswerDTO hcHeight = answers.getById(QuestionsLabelsId.HC_HEIGHT);
		AnswerDTO hcWeight = answers.getById(QuestionsLabelsId.HC_WEIGHT);
		Integer score = 0;
		if (hcHeight != null && hcWeight != null) {
			BigDecimal heightInCm = new BigDecimal(hcHeight.getText().replaceAll("\\D", ""));
			if (heightInCm.doubleValue() > 100) {
				heightInCm = heightInCm.divide(new BigDecimal("100"), mathCtx);
			}
			BigDecimal height = heightInCm;
			BigDecimal weight = new BigDecimal(hcWeight.getNumber());
			double imc = calcImc(height, weight).doubleValue();
			
			if (imc < 18d) {
				score = score + 0;
			}
			if (imc >= 18 && imc < 24.9d) {
				score = score + 0;
			}
			if (imc >= 25 && imc < 29.9d) {
				score = score + 20;
			}
			if (imc >= 30 && imc < 34.9d) {
				score = score + 40;
			}
			if (imc >= 35) {
				score = score + 50;
			}
		}
		
		return score;
	}
	
	private BigDecimal calcImc(BigDecimal height, BigDecimal weight) {
		BigDecimal h = height.multiply(height, mathCtx);
		return weight.divide(h, mathCtx);
	}

}
