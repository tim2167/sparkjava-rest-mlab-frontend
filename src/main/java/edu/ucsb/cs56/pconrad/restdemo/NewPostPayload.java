package edu.ucsb.cs56.pconrad.restdemo;

import lombok.Data;
import java.util.List;
import java.util.LinkedList;

/**
   NewPostPayload is a class that will have getters and
   setters by virtue of Lombok (<a href="https://projectlombok.org/">https://projectlombok.org</a>)
   
   It has title, categories and content.
*/

@Data
class NewPostPayload {
    private String title;
    private List categories = new LinkedList<>();
    private String content;
    
    public boolean isValid() {
	return title != null && !title.isEmpty() && !categories.isEmpty();
    }
}
