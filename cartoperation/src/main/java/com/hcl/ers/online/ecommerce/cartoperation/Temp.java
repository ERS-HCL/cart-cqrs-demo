package com.hcl.ers.online.ecommerce.cartoperation;

import com.hcl.ers.online.ecommerce.event.Event;
import com.hcl.ers.online.ecommerce.event.cart.v1.ItemAddedEvent;

public class Temp {
	public static void main(String[] args) {
		ItemAddedEvent event = new ItemAddedEvent();
		print(event);
	}
	
	private static void print(Event event) {
		System.out.println(((Object)event).getClass().getName());
	}
}
