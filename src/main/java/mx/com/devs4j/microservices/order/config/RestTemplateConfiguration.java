package mx.com.devs4j.microservices.order.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfiguration {

	@Bean
	@LoadBalanced
	@Profile("!test")
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	@Bean
	@Profile("test")
	public RestTemplate nonLoadBalancedRestTemplate() {
		return new RestTemplate();
	}
	
}
