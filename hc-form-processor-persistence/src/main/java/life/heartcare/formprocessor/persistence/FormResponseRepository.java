package life.heartcare.formprocessor.persistence;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import life.heartcare.formprocessor.model.FormResponse;

@Repository
public interface FormResponseRepository extends CrudRepository<FormResponse, Long>{

	List<FormResponse> findByEmailOrderByIdFormResponseDesc(String email);

	FormResponse findTop1ByEmail(String email);
	
}
