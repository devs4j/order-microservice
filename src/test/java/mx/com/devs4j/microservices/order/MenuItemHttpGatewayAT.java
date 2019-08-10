package mx.com.devs4j.microservices.order;

import java.io.IOException;

import org.junit.Rule;
import org.springframework.http.HttpStatus;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactProviderRuleMk2;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.RequestResponsePact;

public class MenuItemHttpGatewayAT {
	
	@Rule
	public PactProviderRuleMk2 provider = new PactProviderRuleMk2("menuItem", "localhost", 8089, this);
	
	@Pact(consumer = "order")
	public RequestResponsePact getMenuItems(PactDslWithProvider provider) throws IOException {
		return provider.given("get all menu items")
				.uponReceiving("when all menu items are requested")
				.path("/items")
				.method("GET")
				.willRespondWith()
				.status(HttpStatus.OK.value())
				.body(TestUtils.readFileAsString("menuItems_getAll.json"))
				.toPact();
	}

}
