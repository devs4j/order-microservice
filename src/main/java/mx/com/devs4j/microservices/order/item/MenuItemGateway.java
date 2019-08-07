package mx.com.devs4j.microservices.order.item;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class MenuItemGateway {

	private RestTemplate restTemplate;
	private String baseUrl;

	public MenuItemGateway(RestTemplate restTemplate, @Value("${menuUrl}") String baseUrl) {
		this.restTemplate = restTemplate;
		this.baseUrl = baseUrl;
	}

	public MenuItem getMenuItem(Integer id) {
		return restTemplate.getForObject(baseUrl + "/" + id, MenuItem.class);
	}

}
