package mx.com.devs4j.microservices.order;

import java.util.List;
import java.util.Set;

import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import mx.com.devs4j.microservices.order.exceptions.OrderNotFoundException;
import mx.com.devs4j.microservices.order.item.MenuItem;
import mx.com.devs4j.microservices.order.item.MenuItemGateway;

@Service
public class OrderService {
	
	private final OrderRepository repository;
	private MenuItemGateway menuItemGateway;
	private CustomChannels customChannels;

	public OrderService(OrderRepository repository, MenuItemGateway menuItemGateway,
			CustomChannels customChannels) {
		this.repository = repository;
		this.menuItemGateway = menuItemGateway;
		this.customChannels = customChannels;
	}
	
	public Order newOrder(@RequestBody Order newOrder) {
		Order savedOrder = repository.save(newOrder);
		customChannels.orderCreated().send(
				MessageBuilder.withPayload(newOrder).build());
		return savedOrder;
	}

	public List<Order> findAll() {
		List<Order> orders = repository.findAll();
		orders.forEach((order) -> {
			fillMenuItemDetails(order.getItems());
		});
		return orders;
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
			MenuItem remoteMenuItem = menuItemGateway.getMenuItem(menuItem.getMenuItemId());
			menuItem.setDescription(remoteMenuItem.getDescription());
			menuItem.setName(remoteMenuItem.getName());
		});
	}

}
