package mx.com.devs4j.microservices.order;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface CustomChannels {

	@Output("order-created")
	MessageChannel orderCreated();

} 