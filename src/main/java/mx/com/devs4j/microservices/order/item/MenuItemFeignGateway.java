package mx.com.devs4j.microservices.order.item;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("menu")
public interface MenuItemFeignGateway {

	@RequestMapping(
		      method= RequestMethod.GET,
		      value="/items/{id}")
	public MenuItem getMenuItem(@PathVariable int id);

}
