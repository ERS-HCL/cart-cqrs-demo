package com.onefoundation.cqrsdemo.cart.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.couchbase.client.java.error.DocumentDoesNotExistException;
import com.onefoundation.cqrsdemo.db.Couchbase;

@Service
public class CartEventSequenceNumber {
	
	String eventSequenceName = "CartEventSequence";
	Couchbase db;
	
	public CartEventSequenceNumber(@Autowired Couchbase db) {
		this.db = db;
		try {
			db.getBucket().counter(eventSequenceName, 0, 1);
		} catch (DocumentDoesNotExistException e) {
			e.printStackTrace();
		}

	}
	
	public long getNextSequenceNumber() {
		long nextIdNumber = db.getBucket().counter(eventSequenceName, 1).content();
		return nextIdNumber;
	}
	
}
