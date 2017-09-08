package com.onefoundation.cqrsdemo.controller;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.couchbase.client.java.document.RawJsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.query.AsyncN1qlQueryResult;
import com.couchbase.client.java.query.N1qlParams;
import com.couchbase.client.java.query.N1qlQuery;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onefoundation.cqrsdemo.db.Couchbase;

@RestController
public class RemoveItemController {
	@Autowired
	Couchbase db;
	
	ObjectMapper mapper = new ObjectMapper();
	
	@Autowired
	CartDAO cartDao;
	
	@RequestMapping(value = "/cart/{cartId}/item/{itemId}", method=RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void handle(@PathVariable("cartId") String cartId, @PathVariable("itemId") String itemId) throws Exception {
				
		Cart cart = cartDao.getCart(cartId);
		
		if (cart == null) {
			throw new IllegalStateException("Cart with cartId="+cartId+" does not exist.");			

		}
		
		List<Item> items = cart.getItems().stream().filter(item -> !item.getId().equals(itemId)).collect(Collectors.toList());
		
		cart.setItems(items);
		cart.setTotalAmount(100);
		cart.setTotalTax(10);
		
		String cartJson = mapper.writeValueAsString(cart);
		
		RawJsonDocument jsonDoc = RawJsonDocument.create(cartId, cartJson);
		
		db.getBucket().upsert(jsonDoc);
		
	}
}
