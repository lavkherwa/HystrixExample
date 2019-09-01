package com.example.hystrix.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BeanManager {

	@Value("${rest.connection.timeout:3000}") // default is 3000ms
	int timeout;

	@Bean
	public RestTemplate restTemplate() {
		HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();

		/*
		 * Partly achieving resilience for our service using timeouts but this not a
		 * solution yet. We will check for further remedies
		 */
		clientHttpRequestFactory.setConnectTimeout(timeout);
		clientHttpRequestFactory.setReadTimeout(timeout);
		clientHttpRequestFactory.setConnectionRequestTimeout(timeout);
		;
		return new RestTemplate(clientHttpRequestFactory);
	}

}
