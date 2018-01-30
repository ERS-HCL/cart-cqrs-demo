package com.hcl.ers.online.ecommerce.ms.price.store;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.query.N1qlParams;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.consistency.ScanConsistency;
import com.hcl.ers.online.ecommerce.event.Event;
import com.hcl.ers.online.ecommerce.eventstore.Couchbase;
import com.hcl.ers.online.ecommerce.eventstore.JsonMapper;

@Service
public class Store {
	
	@Autowired
	JsonMapper jsonMapper;	
	@Autowired
	Couchbase db;
	@Value("${couchbase.bucketName}")
	private String bucketName;
	
	public void upsert(String id, Object object) {
		db.upsert(id, object);
	}
	
	public <T> T get(String id, String docType, Class<T> klass) {
		
		N1qlParams params = N1qlParams.build().adhoc(false).consistency(ScanConsistency.STATEMENT_PLUS);
    	JsonObject values = JsonObject.create().put("id", id);
    	N1qlQuery query = N1qlQuery.parameterized("select "+ bucketName + ".* from `" + bucketName + "` where docType='" + docType + "' and id=$id", values, params);
    	
		List<T> carts = db.get(query, values, params, (json) -> {return jsonMapper.toObject(json, klass);});
		
		if(carts.isEmpty()) {
			return null;
		} else {
			return carts.get(0);
		}
	}
	
	public List<Event> getCartEvents(String cartId, long snapshotEventSequenceNumber) {
		
		N1qlParams params = N1qlParams.build().adhoc(false).consistency(ScanConsistency.STATEMENT_PLUS);
    	JsonObject values = JsonObject.create().put("cartId", cartId)
    						.put("snapshotEventNumber", snapshotEventSequenceNumber);
    	
    	N1qlQuery query = N1qlQuery.parameterized("select "+ bucketName +".* from `"+ bucketName +"` where docType='"+ DocTypes.CartEvent+ "' and cartId=$cartId and eventNumber > $snapshotEventNumber order by eventNumber", values, params);
    	
    	List<Event> events = db.get(query, values, params, (json) -> {return EventBuilder.build(json);});
		 
		return events;
		
	}
	
}
