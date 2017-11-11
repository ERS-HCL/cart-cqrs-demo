package com.onefoundation.cqrsdemo.cart.store;

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
import com.onefoundation.cqrsdemo.cart.command.Event;
import com.onefoundation.cqrsdemo.cart.command.additem.ItemAddedEvent;
import com.onefoundation.cqrsdemo.cart.command.removeitem.ItemUpdatedEvent;

@Service
public class EventStore {
	
	ObjectMapper mapper = new ObjectMapper();
	@Autowired
	Couchbase db;
	
	@Value("${couchbase.eventBucketName}")
	private String eventBucketName;
	
	public List<Event> getCartEvents(String cartId, long snapshotEventSequenceNumber) {
		
		N1qlParams params = N1qlParams.build().adhoc(false).consistency(ScanConsistency.STATEMENT_PLUS);
    	JsonObject values = JsonObject.create().put("cartId", cartId)
    						.put("snapshotEventNumber", snapshotEventSequenceNumber);
    	
    	N1qlQuery query = N1qlQuery.parameterized("select "+ eventBucketName +".* from `"+ eventBucketName +"` where docType='CartEvent' and cartId=$cartId and eventNumber > $snapshotEventNumber order by eventNumber", values, params);
    	
		List<Event> events = db.getBucket().async().query(query)
         .flatMap(AsyncN1qlQueryResult::rows)
         .map(result -> {
			return createEvent(result.value().toString());
		  })
         .toList()
         .timeout(10, TimeUnit.SECONDS)
         .toBlocking()
         .single();
		 
		return events;
		
	}
	

	public void save(List<Event> events) {
		
		for(Event e: events) {
			String es = null;
			try {
				es = mapper.writeValueAsString(e);
			} catch (JsonProcessingException ex) {
				ex.printStackTrace();
			}
			
			RawJsonDocument jsonDoc = RawJsonDocument.create(e.getEventId(), es);
			db.getBucket().insert(jsonDoc);
			
		}
		
	}
	
	private Event createEvent(String json) {
		Event event = null;
		try {
			if(json.contains("ItemUpdatedEvent")) {
				event =  mapper.readValue(json, ItemUpdatedEvent.class);
			} else if (json.contains("ItemAddedEvent")) {
				event =  mapper.readValue(json, ItemAddedEvent.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return event;
	}
	
}
