package com.onefoundation.cqrsdemo.cart;

import java.util.HashMap;
import java.util.Map;

public class Cart {
	
	private String docType = "Cart";
	
	private String id;
	private Map<String, CartItem> items = new HashMap<String, CartItem>();
	private long snapshotEventSequenceNumber;
	
	public Cart() {
	}
	
	public Cart(String id) {
		this.id = id;
	}

	public String getDocType() {
		return docType;
	}

	public String getId() {
		return id;
	}

	public Map<String, CartItem> getItems() {
		return items;
	}

	public long getSnapshotEventSequenceNumber() {
		return snapshotEventSequenceNumber;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setItems(Map<String, CartItem> items) {
		this.items = items;
	}

	public void setSnapshotEventSequenceNumber(long snapshotEventSequenceNumber) {
		this.snapshotEventSequenceNumber = snapshotEventSequenceNumber;
	}
	
}
