package mx.com.devs4j.microservices.order.item;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Table(name = "menu_item")
@Entity
public class MenuItem {

	private @Id Integer id;

	@Transient
	private String name;
	@Transient
	private String description;

	public MenuItem(Integer id) {
		this.id = id;
	}

	public MenuItem() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}