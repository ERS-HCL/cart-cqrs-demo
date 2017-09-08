package com.onefoundation.cqrsdemo.controller;

import java.util.ArrayList;
import java.util.List;

public class Cart {
	
	private String id;
	private final String docType="cart";
	
	private double totalAmount;
	private double totalTax;
	
	private List<Item> items = new ArrayList<Item>();
	
	

	public String getId() {
		return id;
	}

	public void setId(String cartId) {
		this.id = cartId;
	}

	public String getDocType() {
		return docType;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public double getTotalTax() {
		return totalTax;
	}

	public void setTotalTax(double totalTax) {
		this.totalTax = totalTax;
	}
}
