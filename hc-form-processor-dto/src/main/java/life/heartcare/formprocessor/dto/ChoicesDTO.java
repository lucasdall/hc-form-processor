package life.heartcare.formprocessor.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Data;

@Data
public class ChoicesDTO implements TestSelected<String>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1938546290057356889L;
	
	private List<String> labels = new ArrayList<>(0);

	@Override
	public boolean testAny(String... candidates) {
		return labels.stream().map(str -> str.toLowerCase()).anyMatch(Arrays.asList(candidates).stream().map(str -> str.toLowerCase()).collect(Collectors.toList())::contains);
	}

	@Override
	public boolean testAll(String... candidates) {
		return labels.stream().map(str -> str.toLowerCase()).collect(Collectors.toList()).containsAll(Arrays.asList(candidates).stream().map(str -> str.toLowerCase()).collect(Collectors.toList()));
	}

}
