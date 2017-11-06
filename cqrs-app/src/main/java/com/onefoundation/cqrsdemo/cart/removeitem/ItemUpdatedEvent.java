package com.onefoundation.cqrsdemo.cart.removeitem;

import com.onefoundation.cqrsdemo.cart.event.AbstractCartEvent;

public class ItemUpdatedEvent extends AbstractCartEvent {
	
	private String eventName = "ItemUpdatedEvent";
	
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
