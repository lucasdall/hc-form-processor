package life.heartcare.formprocessor.dto;

import lombok.Data;

@Data
public class CheckResponseDTO {

	/**
	 * 
	 */
	public static final long serialVersionUID = 803807158146010646L;

	private Long idFormResponse;

	private String email;
	
	private String channel;
	
	private Integer retryTimeout;
	
	private Integer retryAttempt;
	
	private Boolean found = Boolean.FALSE;
	
	private Boolean finished = Boolean.FALSE;
	
	private String link;
}
