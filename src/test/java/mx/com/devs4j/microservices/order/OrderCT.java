package mx.com.devs4j.microservices.order;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.ResponseCreator;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.RestTemplate;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class OrderCT {

	@Autowired
	private MockMvc mockMvc;

	private MockRestServiceServer mockServer;

	@Autowired
	private RestTemplate restTemplate;

	@Before
	public void setUp() throws IOException {
		mockServer = MockRestServiceServer.bindTo(restTemplate)
				.ignoreExpectOrder(true)
				.build();
	}
	
	@Test
	public void shouldGetOrders() throws Exception {
		mockServer.expect(MockRestRequestMatchers.requestTo("http://mock/items/1"))
		.andRespond(MockRestResponseCreators.withSuccess(
				TestUtils.readFileAsString("menuItems_get1.json"), MediaType.APPLICATION_JSON));
		mockServer.expect(MockRestRequestMatchers.requestTo("http://mock/items/2"))
		.andRespond(MockRestResponseCreators.withSuccess(
				TestUtils.readFileAsString("menuItems_get2.json"), MediaType.APPLICATION_JSON));
		
		mockMvc.perform(MockMvcRequestBuilders.get("/orders"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.jsonPath("$.length()", Matchers.is(1)))
			.andExpect(MockMvcResultMatchers.jsonPath("$[0].items[1].name", Matchers.is("Item1")))
			.andExpect(MockMvcResultMatchers.jsonPath("$[0].items[1].description", Matchers.is("Item1Desc")))
			.andExpect(MockMvcResultMatchers.jsonPath("$[0].items[0].name", Matchers.is("Item2")))
			.andExpect(MockMvcResultMatchers.jsonPath("$[0].items[0].description", Matchers.is("Item2Desc")));
	}

	@Test
	public void shouldGetOrdersEvenIfItemServiceUnavailable() throws Exception {
		mockServer.expect(MockRestRequestMatchers.requestTo("http://mock/items/1"))
		.andRespond(causeTimeout());
		mockServer.expect(MockRestRequestMatchers.requestTo("http://mock/items/2"))
		.andRespond(causeTimeout());
		
		mockMvc.perform(MockMvcRequestBuilders.get("/orders"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.jsonPath("$.length()", Matchers.is(1)))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].items[0].name", Matchers.is("N/A")))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].items[0].description", Matchers.is("N/A")))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].items[1].name", Matchers.is("N/A")))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].items[1].description", Matchers.is("N/A")));
	}
	
	private ResponseCreator causeTimeout() {
		return request -> {
	        try {
	            Thread.sleep(TimeUnit.SECONDS.toMillis(60000));
	        } catch (InterruptedException ignored) {}
	        return null;
	    };
	}

}
