package com.hcl.ers.online.ecommerce.cartoperation.command.removeitem;

import com.hcl.ers.online.ecommerce.cartoperation.command.Command;

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
