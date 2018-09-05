package edu.ucsb.cs56.pconrad.restdemo;

import java.util.Set;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// In a real application you may want to use a DB, for this example we just store the posts in memory

public class Model {
    private int nextId = 1;
    private Map<Integer,Post> posts = new HashMap<Integer,Post>();
    
    
    public int createPost(String title, String content, List categories){
		int id = nextId++;
		Post post = new Post();
		post.setId(id);
		post.setTitle(title);
		post.setContent(content);
		post.setCategories(categories);
		posts.put(id, post);
		return id;
    }
    
    public List getAllPosts(){
		Map<Integer, Post> thePosts = this.posts;
		Set<Integer> keyset = thePosts.keySet();
		Stream<Integer> stream=keyset.stream();
		stream = stream.sorted();
		Stream<Post> postsStream = stream.map((id)->posts.get(id));
		return postsStream.collect(Collectors.toList());
    }
}
