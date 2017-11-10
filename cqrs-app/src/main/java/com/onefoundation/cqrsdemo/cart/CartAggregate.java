package com.onefoundation.cqrsdemo.cart;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.onefoundation.cqrsdemo.cart.command.Event;
import com.onefoundation.cqrsdemo.cart.command.additem.AddItemCommand;
import com.onefoundation.cqrsdemo.cart.command.additem.ItemAddedEvent;
import com.onefoundation.cqrsdemo.cart.command.removeitem.ItemUpdatedEvent;
import com.onefoundation.cqrsdemo.cart.command.removeitem.UpdateItemCommand;
import com.onefoundation.cqrsdemo.cart.store.Cart;
import com.onefoundation.cqrsdemo.cart.store.CartEventNumber;
import com.onefoundation.cqrsdemo.cart.store.CartItem;
import com.onefoundation.cqrsdemo.cart.store.CartStore;
import com.onefoundation.cqrsdemo.cart.store.EventStore;

@Service
@Scope("prototype")
public class CartAggregate {
	
	@Autowired
	private CartEventNumber cartEventNumber;
	@Autowired
	CartStore cartStore;
	private Cart cart;
	@Autowired
	EventStore eventStore;
	String cartId;
	
	public CartAggregate(String cartId) {
		this.cartId = cartId;
	}
	
	public Cart getCart() {
		return cart;
	}
	
	public void refresh() {
		List<Event> events = eventStore.getCartEvents(cart.getId(), cart.getSnapshotEventNumber());
		if(!events.isEmpty()) {
			for (Event e : events) {
				apply(e);
			}
		}
		cartStore.save(cart);
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
		event.setSequenceNumber(cartEventNumber.getNextEventNumber());
		event.setCartId(cart.getId());
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
		event.setSequenceNumber(cartEventNumber.getNextEventNumber());
		event.setCartId(cart.getId());
		event.setQuantity(command.getQuantity());
		event.setSkuId(command.getSkuId());

		events.add(event);
		return events;

	}
	
	@PostConstruct
	private void init() {
		cart = cartStore.get(cartId);
		if (cart == null) {
			cart = new Cart(cartId);
		}
		refresh();
	}
}
