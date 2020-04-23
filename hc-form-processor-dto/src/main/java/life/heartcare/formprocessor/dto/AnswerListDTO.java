package life.heartcare.formprocessor.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import life.heartcare.formprocessor.dto.enums.QuestionsLabelsId;
import lombok.Data;

@Data
public class AnswerListDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5360856161346020638L;

	private Map<String, AnswerDTO> answersMap = new HashMap<>(0);

	public AnswerListDTO(List<AnswerDTO> answersList) {
		if (answersList != null && !answersList.isEmpty()) {
			answersList.forEach(a -> {
				answersMap.putIfAbsent(a.getField().getRef(), a);
			});
		}
	}

	public List<AnswerDTO> list() {
		return (List<AnswerDTO>) answersMap.values();
	}
	
	public AnswerDTO getById(QuestionsLabelsId id) {
		return answersMap.get(id.toString());
	}
	
}
