package life.heartcare.formprocessor.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

@Data
@Builder
public class DetailAnswersDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -835530861204381496L;

	private FormResponseDTO FormResponse;
	
	@Singular("answers")
	private List<AnswerDTO> answers;
	
}
