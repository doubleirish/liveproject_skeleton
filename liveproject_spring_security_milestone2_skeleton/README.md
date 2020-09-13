## Milestone 2 - Secure the Business Logic of an application

### Suggestions
- I submitted a pull request with some minor code fixes for  the skeleton app
https://github.com/lspil/liveproject_skeleton

I also have some more involved suggestions for the skeleton app below. 

#### Skeleton App begins  with security fully disabled

- One  issue with the presupplied skeleton app is that it is already partially secured. 
 Because some dependencies  like the spring-boot-starter-security library are included,
 Spring will auto-configure security with the default UUID generated password in the logs.  
 This makes it difficult for a student to verify that the /metric and /profile endpoints work. 
 
 Disabling security makes it easy to understand how the base app works,
  and also gives students the opportunity to add all those security related libraries and annotations themselves

#### Easily Interrogating the endpoints with Swagger
- building  the REST  requests  to test the /metric and /profile endpoints can be tedious.
  if we enable swagger, the user can quickly test the functioning of the supplied app
  
 http://localhost:7070/swagger-ui/
 
 For example the swagger-ui makes it super easy to build the right JSON to post a metric 
 ```
{
  "id": 6,
  "profile": {
    "id": 2,
    "username": "john"
  },
  "type": "BLOOD_OXYGEN_LEVEL",
  "value": 92
}
```
#### A populated database ready to run. (and a console for troubleshooting)    
- There is a H2 database file  included in the skeleton repo which is referenced from the application.properties. 
  but there's no TABLE schemas or data  population in the database so it's not particularly useful.
  
  I think it may be preferable to use an in-memory database with some data pre-populated
   that always has a known initial state.

 
 # An alternate skeleton starting point 
I also created a branch which has a skeleton app that's a little more friendly for students. 



The main differences are :-
- security disabled - so that users can easily access the /metric /profile endpoints
- swagger enabled - so we can test the endpoints easily
- a populated database - so the app is ready to test immediately 
- a simple dev home page to link to swagger and H2-console pages
     
    

### first steps
- make a fork of the skeleton https://github.com/lspil/liveproject_skeleton/tree/master/liveproject_spring_security_milestone2_skeleton
- from your github repo make a clone onto your workstation
- try a quick build
- run the app
- hit the app endpoints in your browser/swagger/ostman 
- TODO create some postman tests to test the endpoints
- TODO create some unit tests to hit the endpoints 




####  TODO Endpoint for adding a new health profile (POST /profile):
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
