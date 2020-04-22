package life.heartcare.formprocessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan("life.heartcare.formprocessor")
public class FormProcessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(FormProcessorApplication.class, args);
	}

}
