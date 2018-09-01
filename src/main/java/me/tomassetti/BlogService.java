package me.tomassetti;

// SEE: http://sparkjava.com/tutorials/reducing-java-boilerplate

import static spark.Spark.get;
import static spark.Spark.post;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;


import spark.Request;
import spark.Response;
import spark.Route;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;


public class BlogService {
    
    private static final int HTTP_BAD_REQUEST = 400;

    public static void main( String[] args) {
	
	spark.Spark.port(getHerokuAssignedPort());
	
	Model model = new Model();
	
	// insert a post (using HTTP post method)
	post("/posts", (request, response) -> {
		try {
		    ObjectMapper mapper = new ObjectMapper();
		    NewPostPayload creation = mapper.readValue(request.body(), NewPostPayload.class);
		    if (!creation.isValid()) {
			response.status(HTTP_BAD_REQUEST);
			return "";
		    }
		    int id = model.createPost(creation.getTitle(), creation.getContent(), creation.getCategories());
		    response.status(200);
		    response.type("application/json");
		    return id;
		} catch (JsonParseException jpe) {
		    response.status(HTTP_BAD_REQUEST);
		    return "";
		}
	    });
	
	// get all post (using HTTP get method)
	get("/posts", (request, response) -> {
		response.status(200);
		response.type("application/json");
		return dataToJson(model.getAllPosts());
	    });
    }
    
    static int getHerokuAssignedPort() {
	try {
	    ProcessBuilder processBuilder = new ProcessBuilder();
	    if (processBuilder.environment().get("PORT") != null) {
		return Integer.
		    parseInt(processBuilder.environment().get("PORT"));
	    }
	} catch (Exception e) {
	    // fall through to return default port
	}       
        return 4567; // default if PORT isn't set
    }
    

    public static String dataToJson(Object data) {
	try {
	    ObjectMapper mapper = new ObjectMapper();
	    mapper.enable(SerializationFeature.INDENT_OUTPUT);
	    StringWriter sw = new StringWriter();
	    mapper.writeValue(sw, data);
	    return sw.toString();
	} catch (IOException e){
	    throw new RuntimeException("IOException from a StringWriter?");
	}
    }

}

    
