package life.heartcare.formprocessor.dto;

import java.util.Date;

import life.heartcare.formprocessor.dto.enums.Results;
import lombok.Data;

@Data
public class FormResponseDTO {

	/**
	 * 
	 */
	public static final long serialVersionUID = 803807158146010646L;

	private Long idFormResponse;

	private String email;

	private String name;
	
	private String phone;

	private String country;
	
	private String eventId;

	private String eventType;

	private String formId;

	private Date submittedAt;

	private Date savedAt;

	private String payload;

	private Results result;

	private String mailchimpId;
}
