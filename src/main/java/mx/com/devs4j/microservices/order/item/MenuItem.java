package mx.com.devs4j.microservices.order.item;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Table(name = "menu_item")
@Entity
public class MenuItem {

	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Integer id;
	private Integer menuItemId;

	@Transient
	private String name;
	@Transient
	private String description;

	public MenuItem(Integer menuItemId) {
		this.menuItemId = menuItemId;
	}

	public Integer getMenuItemId() {
		return menuItemId;
	}

	public void setMenuItemId(Integer menuItemId) {
		this.menuItemId = menuItemId;
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