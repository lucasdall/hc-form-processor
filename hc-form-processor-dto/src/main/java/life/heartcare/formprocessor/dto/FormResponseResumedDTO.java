package life.heartcare.formprocessor.dto;

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

	private Date submittedAt;

	private Results result;

}
