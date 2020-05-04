package life.heartcare.formprocessor.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import life.heartcare.formprocessor.dto.DetailAnswersDTO;
import life.heartcare.formprocessor.dto.FormResponseDTO;
import life.heartcare.formprocessor.dto.admin.SummaryDTO;
import life.heartcare.formprocessor.dto.enums.Results;
import life.heartcare.formprocessor.service.AdminService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminController {
	
	@Autowired
	private AdminService adminService;
	
	@GetMapping(path = "/")
	public ModelAndView index(ModelMap model) {
		List<SummaryDTO> summaries = adminService.countByType();
		model.addAttribute("summaries", summaries);
		return new ModelAndView("/admin/index", model);
	}

	@GetMapping(path = "/answers/{result}")
	public ModelAndView answers(@PathVariable Results result, ModelMap model) {
		List<FormResponseDTO> forms = adminService.findAllByResult(result);
		model.addAttribute("answers", forms);
		model.addAttribute("result", result);
		return new ModelAndView("/admin/answers", model);
	}
	
	@GetMapping(path = "/detail/{id}")
	public ModelAndView detailById(@PathVariable("id") Long id) throws Exception {
		log.info("begin - detailById - id[{}]", id);
		try {
			DetailAnswersDTO dto = adminService.detailAnswers(id);
			if (dto != null) {
				return new ModelAndView("/admin/detail", "dto", dto);
			} else {
				return new ModelAndView("/admin/detail", HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			log.error("ERROR - detailById - id[{}]", id);
			log.error("ERROR - detailById", e);
			throw e;
		} finally {
			log.info("end - detailById - id[{}]", id);
		}
	}
	
}
