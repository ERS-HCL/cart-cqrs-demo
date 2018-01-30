package com.hcl.ers.online.ecommerce.cartview.model;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.hcl.ers.online.ecommerce.cartview.store.CartStore;
import com.hcl.ers.online.ecommerce.cartview.store.EventStore;
import com.hcl.ers.online.ecommerce.event.Event;
import com.hcl.ers.online.ecommerce.event.cart.v1.ItemAddedEvent;
import com.hcl.ers.online.ecommerce.event.cart.v1.ItemUpdatedEvent;

@Service
@Scope("prototype")
public class CartView {
	
	@Autowired
	CartStore cartStore;
	private Cart cart;
	@Autowired
	EventStore eventStore;
	String cartId;
	
	public CartView(String cartId) {
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
		
		if(cart.getItemMap().containsKey(event.getSkuId())) {
			int newQuantity = cart.getItemMap().get(event.getSkuId()).getQuantity() + event.getQuantity();
			cart.getItemMap().get(event.getSkuId()).setQuantity(newQuantity);
		} else {
			CartItem item = new CartItem(event.getSkuId(), event.getQuantity());
			cart.getItemMap().put(event.getSkuId(), item);
		}
	}
	
	public void apply(ItemUpdatedEvent event) {
		
		if(cart.getItemMap().containsKey(event.getSkuId())) {
			if (event.getQuantity() == 0) {
				cart.getItemMap().remove(event.getSkuId());
			} else {
				cart.getItemMap().get(event.getSkuId()).setQuantity(event.getQuantity());
			}
		}
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
