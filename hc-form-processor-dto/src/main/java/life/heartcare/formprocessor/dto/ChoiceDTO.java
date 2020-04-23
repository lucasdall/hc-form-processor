package life.heartcare.formprocessor.dto;

import java.io.Serializable;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.apache.commons.lang3.NotImplementedException;

import lombok.Data;

@Data
public class ChoiceDTO implements TestSelected<String>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1938546290057356889L;
	
	private String label;

	@Override
	public boolean testAny(String... candidates) {
		return Arrays.asList(candidates).stream().map(str -> str.toLowerCase()).collect(Collectors.toList()).contains(label.toLowerCase());
	}

	@Override
	public boolean testAll(String... candidates) {
		throw new NotImplementedException("not implemented");
	}
	
	

}
