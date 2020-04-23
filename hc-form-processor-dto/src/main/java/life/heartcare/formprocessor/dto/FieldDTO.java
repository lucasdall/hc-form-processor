package life.heartcare.formprocessor.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class FieldDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1938546290057356889L;
	
	private String id;
	
	private String type;
	
	private String ref;

}
