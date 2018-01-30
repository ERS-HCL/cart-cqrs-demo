package com.hcl.ers.online.ecommerce.ms.price.cart.priceview;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hcl.ers.online.ecommerce.ms.price.store.DocTypes;

public class Cart {
	
	public static String docType = DocTypes.PriceMsCartView.name();
	
	private String id;
	private Map<String, CartItem> itemMap = new HashMap<String, CartItem>();
	private long snapshotEventNumber;
	
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

	public void setDocType(String docType) {
		this.docType = docType;
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
