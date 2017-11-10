package com.onefoundation.cqrsdemo.cart.command.additem;

import com.onefoundation.cqrsdemo.cart.command.Command;

public class AddItemCommand implements Command {
	String skuId;
	int quantity;
	
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
