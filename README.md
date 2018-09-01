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
using 




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


