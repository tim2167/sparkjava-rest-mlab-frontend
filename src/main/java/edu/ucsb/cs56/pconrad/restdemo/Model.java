package edu.ucsb.cs56.pconrad.restdemo;

import java.util.Set;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;


import org.apache.log4j.Logger;

import java.io.*;
import java.sql.SQLException;
import java.util.*;

import com.mongodb.client.MongoCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import org.apache.log4j.Logger;
import com.mongodb.client.FindIterable;

// In a real application you may want to use a DB, for this example we just store the posts in memory

public class Model {

	private static Logger log = Logger.getLogger(Model.class.getName());
	
    private int nextId = 1;
    private Map<Integer,Post> posts = new HashMap<Integer,Post>();

	private MongoClientURI uri;
    private MongoClient client;
    private MongoDatabase db;

	private MongoCollection<Document> postCollection;
	
	public Model (String uriString) {
		log.debug("Connecting to MongoDB using uriString="+uriString);
		this.uri = new MongoClientURI(uriString); 
		this.client = new MongoClient(uri);
		this.db = client.getDatabase(uri.getDatabase());
		log.debug("Connected to MongoDB, db="+this.db+" client="+this.client);

		// get a handle to the "posts" collection
        postCollection = this.db.getCollection("posts");
	}
    
    public int createPost(String title, String content, List categories){
		int id = nextId++;
		Post post = new Post();
		post.setId(id);
		post.setTitle(title);
		post.setContent(content);
		post.setCategories(categories);

		String json = BlogService.dataToJson(post);
		postCollection.insertOne(Document.parse(json));
		
		posts.put(id, post);
		return id;
    }
    
    public List<String> getAllPostsJSON(){

		List<String> result = new ArrayList<String>();
		
		FindIterable<Document> docsFound = postCollection.find();

		for (Document cur : docsFound) {
			result.add(cur.toJson());
        }
		
		return result;
    }
}
