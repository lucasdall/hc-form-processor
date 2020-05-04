package life.heartcare.formprocessor.persistence;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import life.heartcare.formprocessor.dto.enums.Results;
import life.heartcare.formprocessor.model.FormResponse;

@Repository
public interface FormResponseRepository extends CrudRepository<FormResponse, Long>{

	List<FormResponse> findByEmailOrderByIdFormResponseDesc(String email);

	FormResponse findTop1ByEmailOrderByIdFormResponseDesc(String email);
	
	@Query("select result as result, count(result) as count from FormResponse group by result order by result asc")
	List<Map<String, Object>> countByType();

	List<FormResponse> findAllByResultOrderByIdFormResponseDesc(Results resul);

}
