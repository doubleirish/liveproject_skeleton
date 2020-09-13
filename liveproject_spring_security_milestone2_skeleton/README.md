## Secure the Business Logic of an application

### Suggestions
- I submitted a PullRequest to resolve a compilation error in the Metric service. 
- I submitted a PullRequest to fix type in HEALTH_METRICS table name ( this causes SQL errors   when POSTing metrics). 
- One  issue with the presupplied skeleton app is that it is already partially secured. 
 because some dependencies  like the spring-boot-starter-security libary are included it used the default  secured moded using a UUID generated password .
 This makes it diffult to for a student to verify that the endpoints work 

  For a Project whose purpose is to concentrate on the security aspects,
   I think it's ok to provide  a skeleton that is a fully working app right out of the box.  
   And let the students focus on addin in all of the necessary security related bits.

- building  the REST  requests  to test the existing   endpoints is tedious.
  if we enable swagger the user can quickly test the functioning of the supplied app
 http://localhost:7070/swagger-ui/
 For example the swagger-ui makes it super easy to build the right JSON to post a metric 
 ```
{
  "id": 0,
  "profile": {
    "id": 0,
    "username": "string"
  },
  "type": "BLOOD_OXYGEN_LEVEL",
  "value": 0
}
```
  
making it difficult to access the app endpoints to verify that they work
- There is a H2 database file  included in the skeleton repo which is referenced from the application.properties. 
  But it's not clear what is in the demo.mv.db file ? 
  Are the  TABLES defined but no data OR are both TABLES and data populated.
  This H2 DB doesn't contain either TABLES or DATA 
  
-  
  
 

### first steps
- make a fork of the skeleton https://github.com/lspil/liveproject_skeleton/tree/master/liveproject_spring_security_milestone2_skeleton
- from your github repo make a clone onto your workstation
- try a quick build
- run the app
- hit the app endpoints in your browser 
  if you're using Intellij Ultimate IDE, Adding the actuator dependency will let you see and interact with those endpoints
  ```
    <dependency>
              <groupId>org.springframework.boot</groupId>
              <artifactId>spring-boot-starter-actuator</artifactId>
          </dependency>
  ```
- create some postman tests to test the endpoints
- create some unit tests to hit the endpoints 




####  Endpoint for adding a new health profile (POST /profile):
*   **Security requirements:** Only the authenticated user can add a profile for themselves. 
Validate that another user cannot add a profile for a different username than its own.
An user can be authenticated only if he previously registered in the system with a username and a password.
#####  
 
 
#####  Endpoint used to get details about a health profile by providing the username as an input value (GET /profile/{username}):
*   **Security requirement:** The user can get the details of their health profile. An admin can get the details of any profile. A non-admin user cannot get details of other users’ profiles. An admin user is a user having authority “admin”.
#####  Endpoint that the client can call to delete a specific health profile by providing the username as an input (DELETE /profile/{username}).
*   **Security requirement:** Only an admin can call this endpoint. An admin user is a user having authority “admin”.
#####  Endpoint that the client can call to add a new health metric record (POST /metric). 
*   **Security requirement:** A health metric record can only be added for the authenticated user.
#####  Endpoint that the client can call to retrieve the health metrics history of a user (GET /metric/{username}). 
*   **Security requirement:** The client can get the health metric records only for the authenticated user.
#####  Endpoint that the client can call to delete the metric history of a user (DELETE /metric/username).
*   **Security requirement:** Only an admin user can call this endpoint to delete the history of any user of the app.
#####  Endpoint that a system can call to send a list of health advice. A health advice has the username and a text description of the advice (POST /advice).
*   **Security requirement:** Someone can call this endpoint using a token generated with the client credentials grant type if they have the scope “advice”.
