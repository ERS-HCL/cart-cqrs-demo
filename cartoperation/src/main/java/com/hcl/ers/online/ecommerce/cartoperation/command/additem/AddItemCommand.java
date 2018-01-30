package com.hcl.ers.online.ecommerce.cartoperation.command.additem;

import com.hcl.ers.online.ecommerce.cartoperation.command.Command;

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
