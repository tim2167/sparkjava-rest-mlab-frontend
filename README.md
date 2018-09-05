<<<<<<< HEAD
# sparkjava-lombok-jackson-example
Code based on http://sparkjava.com/tutorials/reducing-java-boilerplate

# What does this tutorial present?

It shows a way to use:
* Lombok (<https://projectlombok.org>) to reduce the Java boilerplate you need for pure data classes.
   * Basically, you put `@lombok.Data` on your class, and then you don't need to write constructors, getters,
      setters, `toString`, `equals`, `hashCode`, etc.   Lombok does it for you.
   * Note that this feature is coming in later versions of Java (though use of Java beyond Java 8 in the real
      world is still limited, as of Summer 2018.   Java 8 is the "long-term-support" version; Java 11 is only
      just about to become that later in 2018.)
* Jackson and Gson to convert data to/from JSON automatically
* Building a RESTful API (one that "speaks in JSON") using SparkJava

It does NOT show this in the context of a database, but it could be easily extended to, for example, store the
data in a NoSQL database based on JSON such as MongoDB (running, for example, on the free tier of [mlab.com](https://mlab.com/).

# How is the code in this repo modified from the original

* The original is all in one `.java` source file.  I broke it up.
   * All one source file is convenient for a  quick demo example.
   * It is NOT intended as an example of good practice.
* The original isn't set up to be conveniently deployable on Heroku
   * I added the port number settings, a `Procfile`, and the Maven code in the `pom.xml` to enable `mvn heroku:deploy`
* The original uses `Map` instead of `Map<Integer,Post>`, for example, which triggers deprecation warnings, and in one case, a compiler fatal error.
   * I modified the code to remove these issues.


   
# Testing

Since this app is a RESTFUL api that "speaks JSON", you'll need to use special techniques to test it.


The original tutorial shows testing it with a Chrome extension called Postman, but that Chrome extension
appears to be deprecated, and the replacements for it are heavyweight, and require giving access to your
Google account, etc. to unknown parties.  I'd suggest using a different approach.

Here's a tutorial that shows how to test it with plain old curl at the CLI (e.g. the command line on CSIL):

* [Test a REST API with curl](https://www.baeldung.com/curl-rest)

And here's an example that shows how to do the tests originally shown in the tutorial with Postman, but
using curl.

Before we start: let's acknowledge a possible confusion between:
* `POST`, all caps, which is an HTTP method type (`GET` vs. `POST` vs. `PUT`, vs. `DELETE` etc.)
* post, which is an example of a single message on a blog (i.e. a blog post).

Those words are both spelled p-o-s-t, but they are entirely separately concepts.  I'll use `POST` when I mean
the http method, and "post" when I just mean one of the messages on the blog, or the "object" that represents
one of those messages.

Now let's get started. Start the application running on `localhost:4567` in one terminal window using `mvn compile exec:java`.  In a second terminal window, use `curl http://localhost:4567/posts`.   This does a simple `GET` http request to the server, and what is returned in the JSON representation of all the posts currently stored on the server.  


```
$ curl http://localhost:4567/posts
[ ]$ 
```

At the moment, that's an empty list, which in JSON is represneted as `[ ]`.

So if we want some posts in the list, we'll need to add some.

If you `cd` into the directory `testdata`, you'll see that I've created some files that represent
blog posts formatted in JSON.  For example, the contents of the file `post1.json` is this:

```json
{
    "title" : "A post about Spark",
    "content" : "Spark is quite cool!",
    "categories" : ["java","web apps"]
}
```

The curl command can be used with the `-d` option (which stands for data) to do a `POST` request to add this post to the blog.  Here's what that looks like.    This sends a `POST` request to the url, with the payload (content) being the contents of the file `post1.json`:

```
$ curl -d @post1.json http://localhost:4567/posts
1$
```

A few notes about that:

* The response from the server was just the integer `1`; you can see that response before the `$` which is the CLI prompt.  The server responded with the `id` of the object we created.

If we repeat this command a few times, we get `2`, `3`, `4` etc.:

```
$ curl -d @post1.json  http://localhost:4567/posts
3$ curl -d @post1.json  http://localhost:4567/posts
4$
```

If we then simply use `curl http://localhost:4567/posts` again, we get a list of all of these posts formatted in JSON:

```
$ curl http://localhost:4567/posts
[ {
  "id" : 1,
  "title" : "A post about Spark",
  "categories" : [ "java", "web apps" ],
  "content" : "Spark is quite cool!"
}, {
  "id" : 2,
  "title" : "A post about Spark",
  "categories" : [ "java", "web apps" ],
  "content" : "Spark is quite cool!"
}, {
  "id" : 3,
  "title" : "A post about Spark",
  "categories" : [ "java", "web apps" ],
  "content" : "Spark is quite cool!"
}, {
  "id" : 4,
  "title" : "A post about Spark",
  "categories" : [ "java", "web apps" ],
  "content" : "Spark is quite cool!"
} ] $
```

If we stop and restart the webapp, we will see that since this list is just in memory, and not in a database, it does not persist (i.e. stick around).   If we want that, we need to store it in a database with each operation.

So after stopping and restarting the server, once again, we have an empty list:

```
$ curl http://localhost:4567/posts
[ ]$ 
```

To test methods other than `GET` and `POST`, use the `-X` flag.  For example, to
test `DELETE` method, you can use:

```
$ curl -X DELETE http://localhost:4567/posts/13
```


# How to compile and run

| To do this | Do this |
| -----------|-----------|
| run the program | Type `mvn exec:java`.  Visit the web page it indicates in the message |
| check that edits to the pom.xml file are valid | Type `mvn validate` |
| clean up so you can recompile everything  | Type `mvn clean` |
| edit the source code for the app | edit files in `src/main/java`.<br>Under that the directories for the package are `edu/ucsb/cs56/pconrad`  |
| edit the source code for the app | edit files in `src/test/java`.<br>Under that the directories for the package are `edu/ucsb/cs56/pconrad`  |
| compile    | Type `mvn compile` |
| run junit tests | Type `mvn test` |
| build the website, including javadoc | Type `mvn site-deploy` then look in either `target/site/apidocs/index.html`  |
| copy the website to `/docs` for publishing via github-pages | Type `mvn site-deploy` then look for javadoc in `docs/apidocs/index.html` |	
| make a jar file | Type `mvn package` and look in `target/*.jar` |

| run the main in the jar file | Type `java -jar target/sparkjava-demo-01-1.0-jar-with-dependencies.jar ` |
| change which main gets run by the jar | Edit the `<mainClass>` element in `pom.xml` |
| deploy to heroku | change the `<appname>` element and the name of the jar file in both pom.xml and Procfile, then use `heroku login`, then `mvn heroku:deploy` |


