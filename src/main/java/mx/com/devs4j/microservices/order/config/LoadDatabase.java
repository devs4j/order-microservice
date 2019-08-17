package mx.com.devs4j.microservices.order.config;

import java.util.LinkedHashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import mx.com.devs4j.microservices.order.Order;
import mx.com.devs4j.microservices.order.OrderRepository;
import mx.com.devs4j.microservices.order.item.MenuItem;
import mx.com.devs4j.microservices.order.item.MenuItemRepository;

@Configuration
class LoadDatabase {
	
	private final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

	@Bean
	CommandLineRunner initDatabase(OrderRepository repository, MenuItemRepository menuRepository) {
		return args -> {
			Set<MenuItem> items = new LinkedHashSet<>();
			items.add(new MenuItem(1));
			items.add(new MenuItem(2));
			Order order = new Order(items);
			log.info("Preloading " + repository.save(order));
		};
	}
}