package life.heartcare.formprocessor.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import life.heartcare.formprocessor.dto.enums.AnswerType;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnswerDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5360856161346020638L;

	private AnswerType type;
	
	@JsonProperty(value = "boolean")
	private Boolean booleanVal;
	
	private ChoiceDTO choice;
	
	private ChoicesDTO choices;
	
	private FieldDTO field;
	
	@JsonProperty(value = "phone_number")
	private String phoneNumber;
	
	private String email;
	
	private String text;
	
	private String date;
	
	private String number;
	
	private String classStyle;

	public String toString() {
		switch (type) {
		case BOOLEAN:
			return booleanVal.toString();
		case CHOICES:
			return String.join("|", choices.getLabels());
		case CHOICE:
			return choice.getLabel();
		case DATE:
			return date;
		case EMAIL:
			return email;
		case NUMBER:
			return number;
		case PHONE_NUMBER:
			return phoneNumber;
		case TEXT:
			return text;
		default:
			return "";
		}
	}
	
}
