package edu.ucsb.cs56.pconrad.restdemo;


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


	  /**
       return a HashMap with values of all the environment variables
       listed; print error message for each missing one, and exit if any
       of them is not defined.
    */
    
    public static HashMap<String,String> getNeededEnvVars(String [] neededEnvVars) {

        ProcessBuilder processBuilder = new ProcessBuilder();
    		
		HashMap<String,String> envVars = new HashMap<String,String>();
		
		boolean error=false;		
		for (String k:neededEnvVars) {
			String v = processBuilder.environment().get(k);
			if ( v!= null) {
				envVars.put(k,v);
			} else {
				error = true;
				System.err.println("Error: Must define env variable " + k);
			}
        }
		
		if (error) { System.exit(1); }

		System.out.println("envVars=" + envVars);
		return envVars;	 
    }
	
	public static String mongoDBUri(HashMap<String,String> envVars) {

		System.out.println("envVars=" + envVars);
		
		// mongodb://[username:password@]host1[:port1][,host2[:port2],...[,hostN[:portN]]][/[database][?options]]
		String uriString = "mongodb://" +
			envVars.get("MONGODB_USER") + ":" +
			envVars.get("MONGODB_PASS") + "@" +
			envVars.get("MONGODB_HOST") + ":" +
			envVars.get("MONGODB_PORT") + "/" +
			envVars.get("MONGODB_NAME");
		System.out.println("uriString=" + uriString);
		return uriString;
	}
	
    public static void main(String[] args) {
	
		HashMap<String,String> envVars =  
			getNeededEnvVars(new String []{
					"MONGODB_USER",
					"MONGODB_PASS",
					"MONGODB_NAME",
					"MONGODB_HOST",
					"MONGODB_PORT"				
				});
		
		String uriString = mongoDBUri(envVars);

		spark.Spark.port(getHerokuAssignedPort());
	
		Model model = new Model(uriString);
	
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
				return model.getAllPostsJSON().toString();
			});

		get("/",(req,res)->"This is a REST API.  Visit <a href='/posts'><tt>/posts</tt></a>");
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

    
