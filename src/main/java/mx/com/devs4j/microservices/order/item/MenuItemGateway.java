package mx.com.devs4j.microservices.order.item;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

import scala.util.Random;

@Component
public class MenuItemGateway {

	private RestTemplate restTemplate;
	private String baseUrl;

	public MenuItemGateway(RestTemplate restTemplate, @Value("${menuUrl}") String baseUrl) {
		this.restTemplate = restTemplate;
		this.baseUrl = baseUrl;
	}

	@HystrixCommand(commandProperties = {
			@HystrixProperty( name="execution.isolation.thread.timeoutInMilliseconds", value="5000") })
	public MenuItem getMenuItem(Integer id) {
		dormirConProbabilidad1de3();
		return restTemplate.getForObject(baseUrl + "/" + id, MenuItem.class);
	}
	
	private void dormirConProbabilidad1de3() {
		Random rand = new Random();
		int randomNum = rand.nextInt((3 - 1) + 1) + 1;
		if (randomNum == 3) {
			try {
				Thread.sleep(22000);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
	}

}
