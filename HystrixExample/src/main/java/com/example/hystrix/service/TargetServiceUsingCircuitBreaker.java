package com.example.hystrix.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

/*- CIRCUIT BREAKER PARAMETERS
 *  
 *  When does the circuit trip?
 *   # Last n requests to consider the decision
 *   # How many of those failed?
 *   # Timeout duration
 *   
 *  When does the circuit recover?
 *   # How long after a circuit trip to try again?
 *  
 *  Fallback when trip happens?
 *   # Throw error
 *   # Send fallback default response
 *   # send response from CACHE of previous response
 */

@Service
public class TargetServiceUsingCircuitBreaker {

	final RestTemplate restTemplate;

	public TargetServiceUsingCircuitBreaker(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@HystrixCommand(fallbackMethod = "getFallbackMessage", //
			commandProperties = {
					@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000"), /*- Response time of request */
					@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "5"), /*- Look for last 5 calls */
					@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"), /*- If 50% calls are error */
					@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000") /*- Trip will recover after */
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
