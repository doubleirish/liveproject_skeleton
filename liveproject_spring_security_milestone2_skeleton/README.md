## Milestone 2 - Secure the Business Logic of an application
 

###  Milestone 2.1  Endpoint for adding a new health profile (POST /profile):
   **Security requirements:** 
- Only the authenticated user can add a profile for themselves. 
- Validate that another user can not add a profile for a different username than its own.
- An user can be authenticated only if he previously registered in the system with a username and a password.

#### Suggestions
- there are three options of implementing a secured resource server as described in SSIA chapter 14.
 https://livebook.manning.com/book/spring-security-in-action/chapter-14/ 
- we can probably eliminate confusion by indicating in this case we intend to re-use the JWT access token implementation in milestone1.3
- in the event a student was unable to complete milestone 1.3 you will probably need a sample oauth server they can use 
- The student will need to export or publish the public key from the ssia.jks in milestone 1.3
- the following tool is a convenient way of exploring a KeyStore and exporting a public key 
   [https://keystore-explorer.org/index.html](Keystore-explorer) 

#### Steps - 2.1 secure POST /profile

 
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
