package mx.com.devs4j.microservices.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

@Component
public class InventoryListener {
	
	private static final Logger logger = LoggerFactory.getLogger(InventoryListener.class);
	
	@StreamListener("inventory-available")
	public void inventoryAvailable(OrderEvent orderEvent) {
		logger.info("ORDER CONFIRMED {}", orderEvent.getId());
	}
	
	@StreamListener("no-inventory")
	public void noInventory(OrderEvent orderEvent) {
		logger.info("ORDER CANCELLED {}", orderEvent.getId());
	}

}
