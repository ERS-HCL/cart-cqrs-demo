package com.onefoundation.cqrsdemo.cart.store;

public class CartItem {
	
	private String skuId;
	private int quantity;
	
	public CartItem() {
	}
	
	public CartItem(String skuId, int quantity) {
		this.skuId = skuId;
		this.quantity = quantity;
	}

	public String getSkuId() {
		return skuId;
	}

	public int getQuantity() {
		return quantity;
	}
	
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
}
