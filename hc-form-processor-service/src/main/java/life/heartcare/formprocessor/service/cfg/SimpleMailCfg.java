package life.heartcare.formprocessor.service.cfg;

import java.util.concurrent.Executors;

import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.mailer.MailerGenericBuilder;
import org.simplejavamail.springsupport.SimpleJavaMailSpringSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(SimpleJavaMailSpringSupport.class)
public class SimpleMailCfg {

    @SuppressWarnings("rawtypes")
	@Autowired
    private MailerGenericBuilder mailerGenericBuilder;

    @Bean
    public Mailer customMailer() {
        return mailerGenericBuilder
                        .resetThreadPoolSize()
                        .withThreadPoolKeepAliveTime(5000)
                        .withProxyBridgePort(7777)
                        .withExecutorService(Executors.newCachedThreadPool())
                        .buildMailer();
    }
}
