package life.heartcare.formprocessor.service.rules;

import life.heartcare.formprocessor.dto.AnswerListDTO;
import life.heartcare.formprocessor.dto.enums.Results;

public interface RuleValidator {

	boolean match(AnswerListDTO answers);

	Results getResult();
}
