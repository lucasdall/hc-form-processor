package life.heartcare.formprocessor.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

@Data
@Builder
public class DetailInfoDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -835530861204381496L;

	private String linkAlertImg;
	
	private String disclaimer;
	
	@Singular("description")
	private List<String> descriptions;
	
	@Singular("recommendation")
	private List<RecomendationDTO> recommendations;
	
	private Boolean showSymptoms;
	
}
