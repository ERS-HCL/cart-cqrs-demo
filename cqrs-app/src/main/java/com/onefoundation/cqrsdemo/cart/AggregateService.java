package com.onefoundation.cqrsdemo.cart;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.onefoundation.cqrsdemo.cart.event.Event;

@Service
public class AggregateService {
	
	@Autowired
	private CartEventDAO event;
	@Autowired 
	private ApplicationContext applicationContext;
	
	public CartAggregate getCartAggregate(String id) {
		
		CartAggregate cart = applicationContext.getBean(CartAggregate.class, id);
		
		List<Event> events = event.getCartEvents(id);
		
		for(Event e: events) {
			cart.apply(e);
		}
		return cart;
	}
}
