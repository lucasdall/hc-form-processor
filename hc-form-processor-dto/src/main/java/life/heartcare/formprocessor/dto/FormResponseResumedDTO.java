package life.heartcare.formprocessor.dto;

import java.time.LocalDateTime;
import java.util.Date;

import life.heartcare.formprocessor.dto.enums.Results;
import lombok.Data;

@Data
public class FormResponseResumedDTO {

	/**
	 * 
	 */
	public static final long serialVersionUID = 803807158146010646L;

	private Long idFormResponse;

	private LocalDateTime submittedAt;

	private Date savedAt;

	private Results result;

}
