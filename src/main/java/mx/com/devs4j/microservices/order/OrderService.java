package mx.com.devs4j.microservices.order;

import java.util.List;
import java.util.Set;

import org.slf4j.LoggerFactory;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import brave.ScopedSpan;
import brave.Tracer;
import mx.com.devs4j.microservices.order.exceptions.OrderNotFoundException;
import mx.com.devs4j.microservices.order.infrastructure.CustomChannels;
import mx.com.devs4j.microservices.order.item.MenuItem;
import mx.com.devs4j.microservices.order.item.MenuItemGateway;

@Service
public class OrderService {
	
	private final OrderRepository repository;
	private MenuItemGateway menuItemGateway;
	private CustomChannels channels;
	private Tracer tracer;

	public OrderService(OrderRepository repository, MenuItemGateway menuItemGateway, CustomChannels channels, Tracer tracer) {
		this.repository = repository;
		this.menuItemGateway = menuItemGateway;
		this.channels = channels;
		this.tracer = tracer;
	}

	public List<Order> findAll() {
		List<Order> orders = repository.findAll();
		orders.forEach((order) -> {
			fillMenuItemDetails(order.getItems());
		});
		return orders;
	}

	public Order newOrder(@RequestBody Order newOrder) {
		LoggerFactory.getLogger(this.getClass()).info("Creating order");
		Order order = repository.save(newOrder);
		channels.orderCreated().send(MessageBuilder.withPayload(newOrder).build());
		return order;
	}

	public Order findOne(@PathVariable Long id) {
		Order order = repository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
		fillMenuItemDetails(order.getItems());
		return order;
	}

	public Order replaceOrder(@RequestBody Order newOrder, @PathVariable Long id) {
		return repository.findById(id).map(order -> {
			order.setItems(newOrder.getItems());
			return repository.save(order);
		}).orElseGet(() -> {
			newOrder.setId(id);
			return repository.save(newOrder);
		});
	}

	public void deleteOrder(@PathVariable Long id) {
		repository.deleteById(id);
	}
	
	public void fillMenuItemDetails(Set<MenuItem> menuItems) {
		menuItems.forEach((menuItem) -> {
			LoggerFactory.getLogger(this.getClass()).info("Find details of menu item");
			MenuItem remoteMenuItem = menuItemGateway.getMenuItem(menuItem.getMenuItemId());
			ScopedSpan newSpan = tracer.startScopedSpan("establecerValor");
			menuItem.setDescription(remoteMenuItem.getDescription());
			menuItem.setName(remoteMenuItem.getName());
			newSpan.finish();
		});
	}

}
