package life.heartcare.formprocessor.dto.admin;

import java.io.Serializable;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import life.heartcare.formprocessor.dto.enums.Results;
import lombok.Builder;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class SummaryDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5360856161346020638L;

	private Results result;
	
	private Long count;
	
}
