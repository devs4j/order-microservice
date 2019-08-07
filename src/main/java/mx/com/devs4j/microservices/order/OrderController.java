package mx.com.devs4j.microservices.order;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

	private final OrderService service;

	public OrderController(OrderService service) {
		this.service = service;
	}

	@GetMapping("/orders")
	public List<Order> all() {
		return service.findAll();
	}

	@PostMapping("/orders")
	public Order newOrder(@RequestBody Order newMenu) {
		return service.newOrder(newMenu);
	}

	@GetMapping("/orders/{id}")
	public Order one(@PathVariable Long id) {
		return service.findOne(id);
	}

	@PutMapping("/orders/{id}")
	public Order replaceOrder(@RequestBody Order newOrder, @PathVariable Long id) {
		return service.replaceOrder(newOrder, id);
	}

	@DeleteMapping("/orders/{id}")
	public void deleteOrder(@PathVariable Long id) {
		service.deleteOrder(id);
	}
	
}