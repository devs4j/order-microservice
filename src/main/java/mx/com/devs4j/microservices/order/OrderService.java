package mx.com.devs4j.microservices.order;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import mx.com.devs4j.microservices.order.exceptions.OrderNotFoundException;
import mx.com.devs4j.microservices.order.item.MenuItem;
import mx.com.devs4j.microservices.order.item.MenuItemFeignGateway;

@Service
public class OrderService {
	
	private final OrderRepository repository;
	private MenuItemFeignGateway menuItemGateway;

	public OrderService(OrderRepository repository, MenuItemFeignGateway menuItemGateway) {
		this.repository = repository;
		this.menuItemGateway = menuItemGateway;
	}

	public List<Order> findAll() {
		List<Order> orders = repository.findAll();
		orders.forEach((order) -> {
			fillMenuItemDetails(order.getItems());
		});
		return orders;
	}

	public Order newOrder(@RequestBody Order newMenu) {
		return repository.save(newMenu);
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
			MenuItem remoteMenuItem = menuItemGateway.getMenuItem(menuItem.getId());
			menuItem.setDescription(remoteMenuItem.getDescription());
			menuItem.setName(remoteMenuItem.getName());
		});
	}

}
