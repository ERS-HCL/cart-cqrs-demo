package com.onefoundation.cqrsdemo.db;

import org.springframework.stereotype.Component;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;

@Component
public class Couchbase {
	Cluster cluster = null;
	Bucket bucket = null;

	public Couchbase() {
		cluster = CouchbaseCluster.create();
		bucket = cluster.openBucket();
	}

	public Bucket getBucket() {
		return bucket;
	}
}
