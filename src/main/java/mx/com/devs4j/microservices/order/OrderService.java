package mx.com.devs4j.microservices.order;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import brave.ScopedSpan;
import brave.Tracer;
import mx.com.devs4j.microservices.order.exceptions.OrderNotFoundException;
import mx.com.devs4j.microservices.order.item.MenuItem;
import mx.com.devs4j.microservices.order.item.MenuItemGateway;

@Service
public class OrderService {
	
	private final OrderRepository repository;
	private MenuItemGateway menuItemGateway;
	private CustomChannels customChannels;
	private Tracer tracer;
	
	private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

	public OrderService(OrderRepository repository, MenuItemGateway menuItemGateway,
			CustomChannels customChannels, Tracer tracer) {
		this.repository = repository;
		this.menuItemGateway = menuItemGateway;
		this.customChannels = customChannels;
		this.tracer = tracer;
	}
	
	public Order newOrder(@RequestBody Order newOrder) {
		ScopedSpan bdSpan = tracer.startScopedSpan("bdSpan");
		Order savedOrder = repository.save(newOrder);
		bdSpan.finish();
		logger.info("ORDER CREATED");
		OrderEvent orderCreatedEvent = OrderEvent.from(newOrder);
		customChannels.orderCreated().send(
				MessageBuilder.withPayload(orderCreatedEvent).build());
		logger.info("ORDER_CREATED_EVENT_PUBLISHED");
		return savedOrder;
	}

	public List<Order> findAll() {
		logger.info("ORDERS RETRIEVED");
		List<Order> orders = repository.findAll();
		orders.forEach((order) -> {
			fillMenuItemDetails(order.getItems());
		});
		return orders;
	}

	public Order findOne(@PathVariable Integer id) {
		Order order = repository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
		fillMenuItemDetails(order.getItems());
		return order;
	}

	public Order replaceOrder(@RequestBody Order newOrder, @PathVariable Integer id) {
		return repository.findById(id).map(order -> {
			order.setItems(newOrder.getItems());
			return repository.save(order);
		}).orElseGet(() -> {
			newOrder.setId(id);
			return repository.save(newOrder);
		});
	}

	public void deleteOrder(@PathVariable Integer id) {
		repository.deleteById(id);
	}
	
	public void fillMenuItemDetails(Set<MenuItem> menuItems) {
		logger.info("FILLING MENU ITEMS");
		menuItems.forEach((menuItem) -> {
			MenuItem remoteMenuItem = menuItemGateway.getMenuItem(menuItem.getMenuItemId());
			menuItem.setDescription(remoteMenuItem.getDescription());
			menuItem.setName(remoteMenuItem.getName());
		});
	}
	
}
