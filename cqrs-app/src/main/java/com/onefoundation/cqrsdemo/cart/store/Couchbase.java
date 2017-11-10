package com.onefoundation.cqrsdemo.cart.store;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;

@Component
public class Couchbase {
	Cluster cluster = null;
	Bucket bucket = null;
	@Value("${couchbase.connectionString}")
	private String connectionString;

	public Bucket getBucket() {
		return bucket;
	}
	
	@PostConstruct
	public void init() {
		cluster = CouchbaseCluster.create(connectionString);
		bucket = cluster.openBucket();
	}
	
	@PreDestroy
	public void destroy() {
		
	}
}
