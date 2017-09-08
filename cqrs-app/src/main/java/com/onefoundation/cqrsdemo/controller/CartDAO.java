package com.onefoundation.cqrsdemo.controller;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.query.AsyncN1qlQueryResult;
import com.couchbase.client.java.query.N1qlParams;
import com.couchbase.client.java.query.N1qlQuery;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onefoundation.cqrsdemo.db.Couchbase;

@Service
public class CartDAO {
	
	ObjectMapper mapper = new ObjectMapper();
	@Autowired
	Couchbase db;
	

	public Cart getCart(String cartId) throws Exception {
		
		N1qlParams params = N1qlParams.build().adhoc(false);
    	JsonObject values = JsonObject.create().put("cartId", cartId);
    	N1qlQuery query = N1qlQuery.parameterized("select default.* from `default` where docType='cart' and id=$cartId", values, params);
    	
		List<JsonObject> carts = db.getBucket().async().query(query)
         .flatMap(AsyncN1qlQueryResult::rows)
         .map(result -> result.value())
         .toList()
         .timeout(10, TimeUnit.SECONDS)
         .toBlocking()
         .single();
		 
		if (carts.isEmpty()) {
			return null;
		}
		
		Cart cart = mapper.readValue(carts.toArray()[0].toString(), Cart.class);
		return cart;
		
	}
}
