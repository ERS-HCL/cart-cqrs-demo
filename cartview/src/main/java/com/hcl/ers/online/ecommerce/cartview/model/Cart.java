package com.hcl.ers.online.ecommerce.cartview.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Cart {
	
	private String id;
	private Map<String, CartItem> itemMap = new HashMap<String, CartItem>();
	private long snapshotEventNumber;
	
	public Cart() {
	}
	
	public Cart(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
	
	@JsonIgnore
	public Collection<CartItem> getItems() {
		return itemMap.values();
	}
	
	public Map<String, CartItem> getItemMap() {
		return itemMap;
	}

	public long getSnapshotEventNumber() {
		return snapshotEventNumber;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setItemMap(Map<String, CartItem> items) {
		this.itemMap = items;
	}

	public void setSnapshotEventNumber(long snapshotEventNumber) {
		this.snapshotEventNumber = snapshotEventNumber;
	}
	
}
