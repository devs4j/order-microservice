package mx.com.devs4j.microservices.order;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import mx.com.devs4j.microservices.order.item.MenuItem;

@Table(name = "cafeteria_order")
@Entity
public class Order {

	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Integer id;
	
	@JoinColumn(name = "id_order")
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<MenuItem> items = new LinkedHashSet<>();
	
	public Order(Set<MenuItem> items) {
		super();
		this.items = items;
	}
	
	public Order() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Set<MenuItem> getItems() {
		return items;
	}

	public void setItems(Set<MenuItem> items) {
		this.items = items;
	}

}