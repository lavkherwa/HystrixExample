# HystrixExample

This is a simple example implementation of circuit breaker and bulk head pattern used for fault tolerance

In this example of fault tolerance using Hystrix, below patterns are covered:
- [Circuit breaker pattern](HystrixExample/src/main/java/com/example/hystrix/service/TargetServiceUsingCircuitBreaker.java) <br>
Circuit breaker is used when the target service is down. If Hystrix see that target service is down then it will open the circuit and avoid any further call to the target service, this is required to provide sufficient time for the target to recover. In the meanwhile Hystrix will return the fallback message to the consumer. <br>
We can configure when to open the circuit, when to half open the circuit and when to close it back using Hystrix properties
- [Bulk head pattern](https://github.com/lavkherwa/HystrixExample/blob/master/HystrixExample/src/main/java/com/example/hystrix/service/TargetServiceUsingBulkHead.java)<br>
 Bulk head pattern is used in scenarios where we don't want a single slow service to hijack all the available threads and    hence causing the entire application to go down. In bulk head pattern we can restrict the number of threads which can be conusmed by a single service

