package life.heartcare.formprocessor.service.cfg;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Configuration
public class RestTemplateJacksonCfg {

	@Bean
	public RestTemplate restTemplate() {
		RestTemplate rt = new RestTemplate();
		
		rt.setErrorHandler(new ClientErrorHandler());
		
		return rt;
	}

}

@Slf4j
class ClientErrorHandler implements ResponseErrorHandler {

	@Override
	public boolean hasError(ClientHttpResponse response) throws IOException {
	      return response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError();
	}

	@Override
	public void handleError(ClientHttpResponse response) throws IOException {
		log.error("resttemplate handleError status[{}] error[{}]", response.getRawStatusCode(), IOUtils.toString(response.getBody(),StandardCharsets.UTF_8));
	}
	
}