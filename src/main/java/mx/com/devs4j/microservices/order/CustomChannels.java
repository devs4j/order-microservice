package mx.com.devs4j.microservices.order;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface CustomChannels {

	@Output("order-created")
	MessageChannel orderCreated();
	
	@Input("inventory-available")
	SubscribableChannel inventoryAvailable();
	
	@Input("no-inventory")
	SubscribableChannel noInventory();

} 