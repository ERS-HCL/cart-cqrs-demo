package com.onefoundation.cqrsdemo.cart.removeitem;

import com.onefoundation.cqrsdemo.cart.command.Command;

public class UpdateItemCommand implements Command {
	String skuId;
	int quantity;

	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

}
