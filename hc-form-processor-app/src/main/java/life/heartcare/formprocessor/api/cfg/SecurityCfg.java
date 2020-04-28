package life.heartcare.formprocessor.api.cfg;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityCfg extends WebSecurityConfigurerAdapter {

	@Value("${spring.security.user.name}")
	private String API_LOGIN;

	@Value("${spring.security.user.password}")
	private String API_PWD;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
			auth.inMemoryAuthentication()
			.withUser(API_LOGIN).password(encoder.encode(API_PWD)).roles("USER", "ADMIN");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.headers().frameOptions().sameOrigin()
			.and()
			.authorizeRequests()
			.antMatchers("/*", "/api/formprocessor/processing*", "/api/formprocessor/webhook", "/result/*", "/api/formprocessor/findlatest/byemail/*").permitAll()
			.anyRequest().authenticated()
			.and().httpBasic();
	}
}