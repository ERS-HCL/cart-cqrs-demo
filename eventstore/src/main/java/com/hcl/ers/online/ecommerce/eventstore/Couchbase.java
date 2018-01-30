package com.hcl.ers.online.ecommerce.eventstore;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.RawJsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.query.AsyncN1qlQueryResult;
import com.couchbase.client.java.query.N1qlParams;
import com.couchbase.client.java.query.N1qlQuery;

@Component
public class Couchbase {
	
	@Autowired
	private JsonMapper jsonMapper;
	
	Cluster cluster = null;
	Bucket bucket = null;
	@Value("${couchbase.connectionString}")
	private String connectionString;
	
	@Value("${couchbase.username}")
	private String username;
	
	@Value("${couchbase.password}")
	private String password;

	@Value("${couchbase.bucketName}")
	private String bucketName;
	
	public Bucket getBucket() {
		return bucket;
	}
	
	public void upsert(String id, Object object) {
		RawJsonDocument jsonDoc = RawJsonDocument.create(id, jsonMapper.toString(object));
		bucket.upsert(jsonDoc);
	}
	
	public void insert(String id, Object object) {
		RawJsonDocument jsonDoc = RawJsonDocument.create(id, jsonMapper.toString(object));
		bucket.insert(jsonDoc);
	}
	

	
	public <T> List<T> get(N1qlQuery query, JsonObject values, N1qlParams params, Function<String,T> function) {    	
		List<T> objects = bucket.async().query(query)
         .flatMap(AsyncN1qlQueryResult::rows)
         .map(result -> {
        	 return function.apply(result.value().toString());
         })
         .toList()
         .timeout(10, TimeUnit.SECONDS)
         .toBlocking()
         .single();
		
		return objects;
	}
	
	
	@PostConstruct
	public void init() {
		cluster = CouchbaseCluster.create(connectionString);
		cluster.authenticate(username, password);
		bucket = cluster.openBucket(bucketName);
	}
	
	@PreDestroy
	public void destroy() {
		
	}
}
