package com.hcl.ers.online.ecommerce.eventstore;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class JsonMapper {
	private static ObjectMapper mapper = new ObjectMapper();
	
	public <T> T toObject(String json, Class<T> klass) {
		T cart = null;
		try {
			cart = mapper.readValue(json, klass);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return cart;
	}
	
	public String toString(Object object) {
		String json = null;
		try {
			json = mapper.writeValueAsString(object);
		} catch (JsonProcessingException ex) {
			ex.printStackTrace();
		}
		return json;
	}
}
