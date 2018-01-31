package com.hcl.ers.online.ecommerce.cartoperation.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.hcl.ers.online.ecommerce.cartoperation.command.additem.AddItemCommand;
import com.hcl.ers.online.ecommerce.cartoperation.command.removeitem.UpdateItemCommand;
import com.hcl.ers.online.ecommerce.event.Event;
import com.hcl.ers.online.ecommerce.event.cart.v1.ItemAddedEvent;
import com.hcl.ers.online.ecommerce.event.cart.v1.ItemUpdatedEvent;
import com.hcl.ers.online.ecommerce.eventstore.CartEventNumber;
import com.hcl.ers.online.ecommerce.eventstore.Store;

@Service
@Scope("prototype")
public class CartAggregate {
	
	@Autowired
	private CartEventNumber cartEventNumber;
	@Autowired
	Store store;
	private Cart cart;
	String cartId;
	
	public CartAggregate(String cartId) {
		this.cartId = cartId;
	}
	
	public Cart getCart() {
		return cart;
	}
	
	public void refresh() {
		Map<String, String> tags = new HashMap<String, String>();
		tags.put("cartId", cart.getId());
		List<Event> events = store.getEvents(tags, cart.getSnapshotEventNumber());
		if(!events.isEmpty()) {
			for (Event e : events) {
				apply(e);
			}
		}
		store.upsertAggregate(cart.getId(), cart);
	}
	
	public void apply(Event event) {
		if (event == null) {
			return;
		}
		
		if (event instanceof ItemAddedEvent) {
			apply((ItemAddedEvent)event);
		} else if (event instanceof ItemUpdatedEvent) {
			apply((ItemUpdatedEvent)event);
		}
		cart.setSnapshotEventNumber(event.getEventNumber());
	}

	public void apply(ItemAddedEvent event) {
		
		if(cart.getItems().containsKey(event.getSkuId())) {
			int newQuantity = cart.getItems().get(event.getSkuId()).getQuantity() + event.getQuantity();
			cart.getItems().get(event.getSkuId()).setQuantity(newQuantity);
		} else {
			CartItem item = new CartItem(event.getSkuId(), event.getQuantity());
			cart.getItems().put(event.getSkuId(), item);
		}
	}
	
	public void apply(ItemUpdatedEvent event) {
		
		if(cart.getItems().containsKey(event.getSkuId())) {
			if (event.getQuantity() == 0) {
				cart.getItems().remove(event.getSkuId());
			} else {
				cart.getItems().get(event.getSkuId()).setQuantity(event.getQuantity());
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
		event.setEventNumber(cartEventNumber.getNextEventNumber());
		
		event.setTags(createTag(cart.getId()));
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
		
		if(!cart.getItems().containsKey(command.getSkuId())) {
			throw new IllegalStateException("Item {"+command.getSkuId()+"} does not exit in the cart. Can not update item.");
		}
		
		
		List<Event> events = new ArrayList<Event>();
		
		ItemUpdatedEvent event = new ItemUpdatedEvent();
		event.setEventNumber(cartEventNumber.getNextEventNumber());
		event.setTags(createTag(cart.getId()));
		event.setQuantity(command.getQuantity());
		event.setSkuId(command.getSkuId());

		events.add(event);
		return events;

	}
	
	private Map<String, String> createTag(String cartId) {
		Map<String, String> tags = new HashMap<String, String>();
		tags.put("cartId", cartId);
		return tags;
	}
	
	@PostConstruct
	private void init() {
		cart = store.getAggregate(cartId, Cart.class);
		if (cart == null) {
			cart = new Cart(cartId);
		}
		refresh();
	}
}
