package com.hcl.ers.online.ecommerce.eventstore;

import com.couchbase.client.java.document.json.JsonObject;
import com.hcl.ers.online.ecommerce.event.Event;

public class EventBuilder {
	private static JsonMapper mapper = new JsonMapper();

	public static Event build(String json) {
		Event event = null;
		JsonObject jo = JsonObject.fromJson(json);
		try {
			event =  (Event) mapper.toObject(json, Class.forName(jo.getString("eventName")));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return event;
	}
}
