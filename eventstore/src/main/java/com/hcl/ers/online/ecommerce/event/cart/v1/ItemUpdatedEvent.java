package com.hcl.ers.online.ecommerce.event.cart.v1;

public class ItemUpdatedEvent extends AbstractEvent {
	
	private String skuId;
	private int quantity;
	
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
