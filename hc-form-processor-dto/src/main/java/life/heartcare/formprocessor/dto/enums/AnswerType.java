package life.heartcare.formprocessor.dto.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum AnswerType {

	BOOLEAN("boolean"), CHOICE("choice"), TEXT("text"), DATE("date"), NUMBER("number"), CHOICES("choices"), PHONE_NUMBER("phone_number"), EMAIL("email");

	AnswerType(String value) {
		this.value = value;
	}
	
	private String value;
	
	@Override
	@JsonValue
	public String toString() {
		return value;
	}
}
