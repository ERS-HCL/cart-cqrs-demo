package com.onefoundation.cqrsdemo.cart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.onefoundation.cqrsdemo.cart.additem.AddItemCommand;
import com.onefoundation.cqrsdemo.cart.additem.ItemAddedEvent;
import com.onefoundation.cqrsdemo.cart.event.CartEventSequenceNumber;
import com.onefoundation.cqrsdemo.cart.event.Event;
import com.onefoundation.cqrsdemo.cart.removeitem.ItemUpdatedEvent;
import com.onefoundation.cqrsdemo.cart.removeitem.UpdateItemCommand;

@Service
@Scope("prototype")
public class CartAggregate {
	
	@Autowired
	private CartEventSequenceNumber cartEventSequenceNumber;
	
	private String id;
	private Map<String,CartItem> items = new HashMap<String,CartItem>();
	
	public CartAggregate(String id) {
		this.id = id;
	}
	
	
	public String getId() {
		return id;
	}

	public void apply(Event event) {
		if (event instanceof ItemAddedEvent) {
			apply((ItemAddedEvent)event);
		} else if (event instanceof ItemUpdatedEvent) {
			apply((ItemUpdatedEvent)event);
		}
	}

	public void apply(ItemAddedEvent event) {
		
		if(items.containsKey(event.getSkuId())) {
			int newQuantity = items.get(event.getSkuId()).getQuantity() + event.getQuantity();
			items.get(event.getSkuId()).setQuantity(newQuantity);
		} else {
			CartItem item = new CartItem(event.getSkuId(), event.getQuantity());
			items.put(event.getSkuId(), item);
		}
	}
	
	public void apply(ItemUpdatedEvent event) {
		
		if(items.containsKey(event.getSkuId())) {
			if (event.getQuantity() == 0) {
				items.remove(event.getSkuId());
			} else {
				items.get(event.getSkuId()).setQuantity(event.getQuantity());
			}
		}
	}
	
	public List<Event> handle(AddItemCommand command) {
				
		if(command.getSkuId() == null) {
			throw new IllegalArgumentException("skuId field is missing.");
		}
		
		if(command.getQuantity() == 0) {
			throw new IllegalArgumentException("quantity field is missing.");
		}
		
		List<Event> events = new ArrayList<Event>();
		
		ItemAddedEvent event = new ItemAddedEvent();
		event.setSequenceNumber(cartEventSequenceNumber.getNextSequenceNumber());
		event.setCartId(id);
		event.setQuantity(command.getQuantity());
		event.setSkuId(command.getSkuId());
		
		events.add(event);
		return events;
	}
	
	public List<Event> handle(UpdateItemCommand command) {
		if (command.getSkuId() == null) {
			throw new IllegalArgumentException("skuId field is missing.");
		}
		
		if(command.getQuantity() <= 0) {
			throw new IllegalArgumentException("New quantity can not be negative.");
		}
		
		if(!items.containsKey(command.getSkuId())) {
			throw new IllegalStateException("Item {"+command.getSkuId()+"} does not exit in the cart. Can not update item.");
		}
		
		
		List<Event> events = new ArrayList<Event>();
		
		ItemUpdatedEvent event = new ItemUpdatedEvent();
		event.setSequenceNumber(cartEventSequenceNumber.getNextSequenceNumber());
		event.setCartId(id);
		event.setQuantity(command.getQuantity());
		event.setSkuId(command.getSkuId());

		events.add(event);
		return events;

	}
	
	public CartAggregate newInstance(String id) {
		
		return null;
	}
	
	public Map<String,CartItem> getItems() {
		return this.items;
	}
	
}
