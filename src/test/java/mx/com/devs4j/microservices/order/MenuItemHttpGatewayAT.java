package mx.com.devs4j.microservices.order;

import java.io.IOException;
import java.util.HashMap;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactProviderRuleMk2;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.RequestResponsePact;
import mx.com.devs4j.microservices.order.item.MenuItem;
import mx.com.devs4j.microservices.order.item.MenuItemGateway;

public class MenuItemHttpGatewayAT {
	
	@Rule
	public PactProviderRuleMk2 provider = new PactProviderRuleMk2("menuItem", "localhost", 8089, this);
	
	@Pact(consumer = "order")
	public RequestResponsePact getMenuItems(PactDslWithProvider provider) throws IOException {
		HashMap<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		
		return provider.given("get one menu item")
				.uponReceiving("when one menu item is requested")
				.path("/items/1")
				.method("GET")
				.willRespondWith()
				.status(HttpStatus.OK.value())
				.headers(headers)
				.body(TestUtils.readFileAsString("menuItems_getAll.json"))
				.toPact();
	}
	
	@Test
	@PactVerification(value = "menuItem", fragment="getMenuItems")
	public void shouldGetAllMenuItems() {
		MenuItemGateway menuItemGateway = new MenuItemGateway(getRestTemplate(), 
				provider.getUrl() + "/items");
		
		MenuItem menuItem = menuItemGateway.getMenuItem(1);
		
		Assert.assertEquals(Integer.valueOf(1), menuItem.getId());
	}
	
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

}
