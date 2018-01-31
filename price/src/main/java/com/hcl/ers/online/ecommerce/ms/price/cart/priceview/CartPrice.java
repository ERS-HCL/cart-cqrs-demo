package com.hcl.ers.online.ecommerce.ms.price.cart.priceview;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CartPrice {
	
	private String id;
	private float totalAmount;
	private Map<String, CartItemPrice> itemMap = new HashMap<String, CartItemPrice>();
	
	public CartPrice() {
	}
	
	public CartPrice(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
	
	public Collection<CartItemPrice> getItems() {
		return itemMap.values();
	}
	

	public void setId(String id) {
		this.id = id;
	}

	
	public void addItemPrice(CartItemPrice cartItemPrice) {
		itemMap.put(cartItemPrice.getSkuId(), cartItemPrice);
	}

	public float getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(float totalAmount) {
		this.totalAmount = totalAmount;
	}
	
}
