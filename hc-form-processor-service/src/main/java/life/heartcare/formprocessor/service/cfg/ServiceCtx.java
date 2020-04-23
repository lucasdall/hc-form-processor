package life.heartcare.formprocessor.service.cfg;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import life.heartcare.formprocessor.persistence.cfg.PersistenceCtx;

@Configuration
@ComponentScan("life.heartcare.formprocessor")
@Import(PersistenceCtx.class)
public class ServiceCtx {

}
