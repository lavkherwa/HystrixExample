package com.example.hystrix.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.hystrix.service.TargetServiceUsingBulkHead;
import com.example.hystrix.service.TargetServiceUsingCircuitBreaker;

@RestController
@RequestMapping("/api")
public class ApiController {

	final TargetServiceUsingCircuitBreaker serviceCircuitBreaker;
	final TargetServiceUsingBulkHead serviceBulkHead;

	public ApiController(TargetServiceUsingCircuitBreaker service, TargetServiceUsingBulkHead serviceBulkHead) {
		this.serviceCircuitBreaker = service;
		this.serviceBulkHead = serviceBulkHead;
	}

	@GetMapping("/target1")
	public String callTarget1() {
		return serviceCircuitBreaker.callTarget();
	}

	@GetMapping("/target2")
	public String callTarget2() {
		return serviceBulkHead.callTarget();
	}

}
