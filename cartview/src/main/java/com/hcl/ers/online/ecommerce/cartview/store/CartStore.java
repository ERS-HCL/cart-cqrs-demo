package com.hcl.ers.online.ecommerce.cartview.store;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.couchbase.client.java.document.RawJsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.query.AsyncN1qlQueryResult;
import com.couchbase.client.java.query.N1qlParams;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.consistency.ScanConsistency;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.ers.online.ecommerce.cartview.model.Cart;
import com.hcl.ers.online.ecommerce.eventstore.Couchbase;

@Service
public class CartStore {
	
	ObjectMapper mapper = new ObjectMapper();	
	@Autowired
	Couchbase db;
	@Value("${couchbase.bucketName}")
	private String bucketName;
	
	public void save(Cart cart) {
		String json = null;
		try {
			json = mapper.writeValueAsString(cart);
		} catch (JsonProcessingException ex) {
			ex.printStackTrace();
		}

		RawJsonDocument jsonDoc = RawJsonDocument.create(cart.getId(), json);
		db.getBucket().upsert(jsonDoc);
	}
	
	public Cart get(String cartId) {
		
		N1qlParams params = N1qlParams.build().adhoc(false).consistency(ScanConsistency.STATEMENT_PLUS);
    	JsonObject values = JsonObject.create().put("id", cartId);
    	N1qlQuery query = N1qlQuery.parameterized("select "+ bucketName + ".* from `" + bucketName + "` where docType='" + DocTypes.CartQueryView.name()+ "' and id=$id", values, params);
    	
		List<Cart> carts = db.getBucket().async().query(query)
         .flatMap(AsyncN1qlQueryResult::rows)
         .map(result -> {
			return createObject(result.value().toString());
		  })
         .toList()
         .timeout(10, TimeUnit.SECONDS)
         .toBlocking()
         .single();
		
		if(carts.isEmpty()) {
			return null;
		} else {
			return carts.get(0);
		}
	}
	
	private Cart createObject(String json) {
		Cart cart = null;
		try {
			cart = mapper.readValue(json, Cart.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return cart;
	}
}
