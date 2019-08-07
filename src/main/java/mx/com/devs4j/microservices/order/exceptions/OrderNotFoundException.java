package mx.com.devs4j.microservices.order.exceptions;

public class OrderNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 7111845800579755015L;

	public OrderNotFoundException(Long id) {
		super("Could not find order " + id);
	}
	
}