package life.heartcare.formprocessor.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import life.heartcare.formprocessor.dto.enums.Results;
import lombok.Data;

@Data
public class FormResponseEmailDTO {

	/**
	 * 
	 */
	public static final long serialVersionUID = 803807158146010646L;

	private Long idFormResponse;

	private String email;

	private Date submittedAt;

	private String payload;

	private Results result;

	private String name;
	
	private List<String> symptoms = new ArrayList<>(0);
}