package life.heartcare.formprocessor.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

@Data
@Builder
public class RecomendationDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -835530861204381496L;
	
	private String title;
	
	@Singular("recommendation")
	private List<String> recommendations;
	
}
