package com.onefoundation.cqrsdemo.cart.additem;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onefoundation.cqrsdemo.cart.AggregateService;
import com.onefoundation.cqrsdemo.cart.CartAggregate;
import com.onefoundation.cqrsdemo.cart.CartEventDAO;
import com.onefoundation.cqrsdemo.cart.event.Event;

@RestController
public class AddItemController {
	@Autowired
	AggregateService aggregateService;
	@Autowired
	CartEventDAO eventDAO;
	
	ObjectMapper mapper = new ObjectMapper();
	
	@RequestMapping(value = "/cart/{cartId}/item/add", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public Object handle(@PathVariable("cartId") String cartId, @RequestBody AddItemCommand addItemCommand) {
		
		CartAggregate cart = aggregateService.getCartAggregate(cartId);
		List<Event> events = cart.handle(addItemCommand);
		eventDAO.save(events);
		return cart.getItems();
	}

}
