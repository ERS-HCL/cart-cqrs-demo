package com.onefoundation.cqrsdemo.cart.additem;

import com.onefoundation.cqrsdemo.cart.event.AbstractCartEvent;

public class ItemAddedEvent extends AbstractCartEvent {
	
	private String eventName = "ItemAddedEvent";
	
	private String skuId;
	private int quantity;
	
	@Override
	public String getEventName() {
		return eventName;
	}
	
	@Override
	public void setEventName(String eventName) {
		this.eventName = eventName;
		
	}
	
	public String getSkuId() {
		return skuId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

}
