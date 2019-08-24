package mx.com.devs4j.microservices.order;

import java.util.List;
import java.util.stream.Collectors;

public class OrderEvent {
	
	private Integer id;
	private List<Integer> menuItemsIds;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public List<Integer> getMenuItemsIds() {
		return menuItemsIds;
	}
	public void setMenuItemsIds(List<Integer> menuItemsIds) {
		this.menuItemsIds = menuItemsIds;
	}
	
	public static OrderEvent from(Order order) {
		OrderEvent orderEvent = new OrderEvent();
		orderEvent.setId(order.getId());
		orderEvent.setMenuItemsIds(order.getItems().stream().map((mi) -> mi.getMenuItemId()).collect(Collectors.toList()));
		return orderEvent;
	}

}
