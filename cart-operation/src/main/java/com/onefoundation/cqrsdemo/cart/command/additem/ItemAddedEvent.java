package com.onefoundation.cqrsdemo.cart.command.additem;

import com.onefoundation.cqrsdemo.cart.command.AbstractCartEvent;
import com.onefoundation.cqrsdemo.cart.command.EventNames;

public class ItemAddedEvent extends AbstractCartEvent {
	
	private String eventName = EventNames.ItemAddedEvent.name();
	
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
