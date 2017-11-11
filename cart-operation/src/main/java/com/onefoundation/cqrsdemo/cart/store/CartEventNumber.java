package com.onefoundation.cqrsdemo.cart.store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.couchbase.client.java.error.DocumentDoesNotExistException;

@Service
public class CartEventNumber {
	
	String eventNumberSequenceName = "CartEventNumber";
	Couchbase db;
	
	public CartEventNumber(@Autowired Couchbase db) {
		this.db = db;
		try {
			db.getBucket().counter(eventNumberSequenceName, 0, 0);
		} catch (DocumentDoesNotExistException e) {
			e.printStackTrace();
		}

	}
	
	public long getNextEventNumber() {
		long nextIdNumber = db.getBucket().counter(eventNumberSequenceName, 1).content();
		return nextIdNumber;
	}
	
}
