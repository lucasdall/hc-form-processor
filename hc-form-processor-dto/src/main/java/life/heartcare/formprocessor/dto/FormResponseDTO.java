package life.heartcare.formprocessor.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

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

	private String eventId;

	private String eventType;

	private String formId;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone = JsonFormat.DEFAULT_TIMEZONE)
	private Date submittedAt;

	private String payload;

	private Results result;

	
}
