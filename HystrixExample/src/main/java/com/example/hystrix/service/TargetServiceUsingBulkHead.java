package com.example.hystrix.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

/*
 * When some services are down we don't want all the threads 
 * to be occupied to serve the response for that single service 
 * and make other services suffer. So, we can assign a definite thread pool
 * for the given service and avoid such embarrassing situations
 */

@Service
public class TargetServiceUsingBulkHead {

	final RestTemplate restTemplate;

	public TargetServiceUsingBulkHead(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@HystrixCommand(fallbackMethod = "getFallbackMessage", //
			threadPoolKey = "messageService", // thread pool defined with given key
			threadPoolProperties = { //
					@HystrixProperty(name = "coreSize", value = "20"), // Threads size
					@HystrixProperty(name = "maxQueueSize", value = "10") // Wait before consuming thread
			})
	public String callTarget() {

		String response = null;
		/*- Calling another service on localhost:8080 
		 *  This is another example of AllAboutRest - TestController
		 */
		try {
			response = restTemplate.getForObject("http://localhost:8080/api/message", String.class);
		} catch (HttpClientErrorException exception) {
			response = "Message: " + exception.getMessage();
		}

		return response;
	}

	/*
	 * Note: Fallback method should have the same signature as the actual annotated
	 * method
	 */
	public String getFallbackMessage() {
		return "Target service is down";
	}

}
