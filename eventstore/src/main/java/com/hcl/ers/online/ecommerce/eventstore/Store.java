package com.hcl.ers.online.ecommerce.eventstore;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.query.N1qlParams;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.consistency.ScanConsistency;
import com.hcl.ers.online.ecommerce.event.Event;

@Service
public class Store {
	
	@Autowired
	JsonMapper jsonMapper;	
	@Autowired
	Couchbase db;
	@Value("${couchbase.bucketName}")
	private String bucketName;
	
	public void upsertAggregate(String id, Object object) {
		JsonObject jo = JsonObject.fromJson(jsonMapper.toString(object));
		jo.put(Keys.docType.name(), object.getClass().getName());
		db.upsert(id, jo);
	}
		
	public <T> T getAggregate(String id, Class<T> klass) {
		
		N1qlParams params = N1qlParams.build().adhoc(false).consistency(ScanConsistency.STATEMENT_PLUS);
    	JsonObject values = JsonObject.create().put("id", id);
    	N1qlQuery query = N1qlQuery.parameterized("select "+ bucketName + ".* from `" + bucketName + "` where " + Keys.docType.name() + "='" + klass.getName() + "' and id=$id", values, params);
    	
		List<T> aggregate = db.get(query, values, params, (json) -> {return jsonMapper.toObject(json, klass);});
		
		if(aggregate.isEmpty()) {
			return null;
		} else {
			return aggregate.get(0);
		}
	}
	
	public void insertEvent(Event event) {
		JsonObject jo = JsonObject.fromJson(jsonMapper.toString(event));
		jo.put(Keys.docType.name(), Event.class.getName());
		jo.put(Keys.eventName.name(), event.getClass().getName());
		db.insert(event.getEventId(), jo);
	}
	
	public void insertEvent(List<Event> events) {
		for(Event event: events) {
			insertEvent(event);
		}
	}
	
	public List<Event> getEvents(Map<String, String> tags, long snapshotEventNumber) {
		
		N1qlParams params = N1qlParams.build().adhoc(false).consistency(ScanConsistency.STATEMENT_PLUS);
		
		String q = "select "+ bucketName +".* from `"+ bucketName +"` where " + Keys.docType.name() + "='"+ Event.class.getName() +"'";
		JsonObject values = JsonObject.create();
		for(Entry<String, String> tag: tags.entrySet()) {
			values.put(tag.getKey(), tag.getValue());
			q = q + " and tags." + tag.getKey() + "=$" + tag.getKey();
		}
		
    	values.put("snapshotEventNumber", snapshotEventNumber);
    	q = q + " and eventNumber > $snapshotEventNumber order by eventNumber";
    	
    	N1qlQuery query = N1qlQuery.parameterized(q, values, params);
    	
    	List<Event> events = db.get(query, values, params, (json) -> {return EventBuilder.build(json);});
		 
		return events;
		
	}
	
}
