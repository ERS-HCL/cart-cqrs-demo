package com.hcl.ers.online.ecommerce.cartview.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.hcl.ers.online.ecommerce.event.Event;
import com.hcl.ers.online.ecommerce.event.cart.v1.ItemAddedEvent;
import com.hcl.ers.online.ecommerce.event.cart.v1.ItemUpdatedEvent;
import com.hcl.ers.online.ecommerce.eventstore.Store;

@Service
@Scope("prototype")
public class CartView {
	
	@Autowired
	private Store store;
	
	private Cart cart;
	String cartId;
	
	public CartView(String cartId) {
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
		cart = store.getAggregate(cartId, Cart.class);
		if (cart == null) {
			cart = new Cart(cartId);
		}
		refresh();
	}
}
