package com.hcl.ers.online.ecommerce.cartoperation.command.additem;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.ers.online.ecommerce.cartoperation.model.CartAggregate;
import com.hcl.ers.online.ecommerce.cartoperation.store.EventStore;
import com.hcl.ers.online.ecommerce.event.Event;

@RestController
public class AddItemController {
	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
	EventStore eventStore;
	ObjectMapper mapper = new ObjectMapper();
	
	@RequestMapping(value = "/ms/cartoperation/{cartId}/item/add", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public Object handle(@PathVariable("cartId") String cartId, @RequestBody AddItemCommand addItemCommand) {
		CartAggregate cart = applicationContext.getBean(CartAggregate.class, cartId);
		List<Event> events = cart.handle(addItemCommand);
		eventStore.save(events);
		
		cart.refresh();
		return cart.getCart();
	}

}
