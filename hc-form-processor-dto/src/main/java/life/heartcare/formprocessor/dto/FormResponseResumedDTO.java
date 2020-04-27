package life.heartcare.formprocessor.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import life.heartcare.formprocessor.dto.enums.Results;
import lombok.Data;

@Data
public class FormResponseResumedDTO {

	/**
	 * 
	 */
	public static final long serialVersionUID = 803807158146010646L;

	private Long idFormResponse;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone = "America/Sao_Paulo")
	private Date submittedAt;

	private Results result;

}
