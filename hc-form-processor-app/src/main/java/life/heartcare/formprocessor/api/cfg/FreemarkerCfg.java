package life.heartcare.formprocessor.api.cfg;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import freemarker.template.Template;

@Configuration
public class FreemarkerCfg {

//    @Bean
//    public FreeMarkerConfigurationFactoryBean getFreeMarkerConfiguration() {
//        FreeMarkerConfigurationFactoryBean bean = new FreeMarkerConfigurationFactoryBean();
//        bean.setTemplateLoaderPath("/templates/");
//        return bean;
//    }
	
	@Bean
	public Template emailResult(FreeMarkerConfig freemarkerConfig) throws Throwable {
        Template t = freemarkerConfig.getConfiguration().getTemplate("result-email.ftl");
        return t;
	}
	
}
