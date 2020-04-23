package life.heartcare.formprocessor.dto;

public interface TestSelected<T> {

	@SuppressWarnings("unchecked")
	boolean testAny(T... candidates);

	@SuppressWarnings("unchecked")
	boolean testAll(T... candidates);
	
}
