package com.hcl.ers.online.ecommerce.ms.price.cart.priceview;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.hcl.ers.online.ecommerce.event.Event;
import com.hcl.ers.online.ecommerce.event.cart.v1.ItemAddedEvent;
import com.hcl.ers.online.ecommerce.event.cart.v1.ItemUpdatedEvent;
import com.hcl.ers.online.ecommerce.eventstore.Store;
import com.hcl.ers.online.ecommerce.ms.price.cart.CartPriceService;

@Service
@Scope("prototype")
public class CartPriceView {
	
	@Autowired
	private Store store;
	@Autowired
	private CartPriceService cartPriceService;
	
	private Cart cart;
	private CartPrice cartPrice;
		
	String cartId;
	
	public void load(String cartId) {
		this.cartId = cartId;
		cart = store.getAggregate(cartId, Cart.class);
		cartPrice = store.getAggregate(cartId, CartPrice.class);
		if (cart == null) {
			cart = new Cart(cartId);
		}
		refresh();
	}
	
	public CartPrice getCartPrice() {
		if(cart == null) {
			throw new IllegalStateException("Call load method first.");
		}
		
		return cartPrice;
	}
	
	public void refresh() {
		Map<String, String> tags = new HashMap<String, String>();
		tags.put("cartId", cart.getId());
		List<Event> events = store.getEvents(tags, cart.getSnapshotEventNumber());
		if(!events.isEmpty()) {
			for (Event e : events) {
				apply(e);
			}
			
			cartPrice = cartPriceService.calculatePrice(cart);
			store.upsertAggregate(cart.getId(), cart);
			store.upsertAggregate(cart.getId(), cartPrice);
		}
		
	}
	
	private void apply(Event event) {
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

	private void apply(ItemAddedEvent event) {
		
		if(cart.getItemMap().containsKey(event.getSkuId())) {
			int newQuantity = cart.getItemMap().get(event.getSkuId()).getQuantity() + event.getQuantity();
			cart.getItemMap().get(event.getSkuId()).setQuantity(newQuantity);
		} else {
			CartItem item = new CartItem(event.getSkuId(), event.getQuantity());
			cart.getItemMap().put(event.getSkuId(), item);
		}
	}
	
	private void apply(ItemUpdatedEvent event) {
		
		if(cart.getItemMap().containsKey(event.getSkuId())) {
			if (event.getQuantity() == 0) {
				cart.getItemMap().remove(event.getSkuId());
			} else {
				cart.getItemMap().get(event.getSkuId()).setQuantity(event.getQuantity());
			}
		}
	}
	
}
