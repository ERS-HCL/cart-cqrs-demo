package com.hcl.ers.online.ecommerce.cartoperation.model;

import java.util.HashMap;
import java.util.Map;

public class Cart {
	
	private String id;
	private Map<String, CartItem> items = new HashMap<String, CartItem>();
	private long snapshotEventNumber;
	
	public Cart() {
	}
	
	public Cart(String id) {
		this.id = id;
	}


	public String getId() {
		return id;
	}

	public Map<String, CartItem> getItems() {
		return items;
	}

	public long getSnapshotEventNumber() {
		return snapshotEventNumber;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setItems(Map<String, CartItem> items) {
		this.items = items;
	}

	public void setSnapshotEventNumber(long snapshotEventNumber) {
		this.snapshotEventNumber = snapshotEventNumber;
	}
	
}
