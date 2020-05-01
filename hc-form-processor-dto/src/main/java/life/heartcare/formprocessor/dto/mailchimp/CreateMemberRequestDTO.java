package life.heartcare.formprocessor.dto.mailchimp;

import java.io.Serializable;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import life.heartcare.formprocessor.dto.mailchimp.enums.Language;
import life.heartcare.formprocessor.dto.mailchimp.enums.MergeField;
import life.heartcare.formprocessor.dto.mailchimp.enums.Status;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

@Data
@Builder
public class CreateMemberRequestDTO implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 2522528314909354608L;

	@JsonProperty("email_address")
	private String emailAddress;
	
	private Status status;
	
	private Language language;
	
	@Singular
	@JsonProperty("merge_fields")
	private Map<MergeField, String> mergeFields;
	
}
