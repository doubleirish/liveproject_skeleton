## Milestone 2 - Secure the Business Logic of an application
 

###  Milestone 2.1  Endpoint for adding a new health profile (POST /profile):
   **Security requirements:** 
- Only the authenticated user can add a profile for themselves. 
- Validate that another user can not add a profile for a different username than its own.
- An user can be authenticated only if he previously registered in the system with a username and a password.

####  mini milestone Timings
(provided)
1. build enhanced skeleton with swagger, populated db, H2 console, postman tests (8 hours)


1. (step 0) secure the resource server with JWT public key -> research and coding (2 hours)

mini milestones
1. creating health profile for self only-> enabling method security , creating SPel and postman tests (1 hour)
1. finding any health profile when admin user.  @PreAuthorize and  postman tests (1 hour)
1. only a user with admin role can delete a health profile.  @PreAuthorize and  postman tests (1 hour)



#### Suggestions
- there are three options of implementing a secured resource server as described in SSIA chapter 14.
 https://livebook.manning.com/book/spring-security-in-action/chapter-14/ 
- we can probably eliminate confusion by indicating in this case we intend to re-use the JWT access token implementation in milestone1.3
- in the event a student was unable to complete milestone 1.3 you will probably need a sample oauth server they can use 
- The student will need to export or publish the public key from the ssia.jks in milestone 1.3
- the following tool is another convenient way of exploring a KeyStore and exporting a public key 
   [https://keystore-explorer.org/index.html](Keystore-explorer) 

### Steps - 2.1 secure POST /profile

#### review SSIA section 15.2.3

https://livebook.manning.com/book/spring-security-in-action/chapter-15/v-7/117

#### add the security related dependencies 

```  
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-oauth2</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-data</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-jwt</artifactId>
            <version>1.1.1.RELEASE</version>
        </dependency>


   <dependency>
      <groupId>org.springframework.security</groupId>
      <artifactId>spring-security-data</artifactId>
   </dependency>

   <dependency>
      <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-test</artifactId>
      <scope>test</scope>
   </dependency>
  
```
and the cloud BOM
```
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>Hoxton.SR1</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
```

#### add a public key 
extract the public key from the ssia.jks keystore created in milestone 1.3
```
keytool -list -rfc --keystore ssia.jks | openssl x509 -inform pem -pubkey
```
and save it as file ssia.pem in src/main/resources so you can later inject it into your JWT bean

alternatively you can publish the public key in your OAuth server and load it from the resource server


#### define the Configuration for a ResourceServer
```
package com.laurentiuspilca.liveproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import static com.nimbusds.jose.util.IOUtils.readInputStreamToString;

@Configuration
@EnableResourceServer
public class ResourceServerConfig
        extends ResourceServerConfigurerAdapter {
   // you can also load in  key as an  @Value by pasting the PEM into a field in application.properties 
    @Bean  
    public String publicKey()  {
        String publicKey;
        try {
            publicKey = readInputStreamToString(new ClassPathResource("ssia.pem").getInputStream());
        } catch (Exception e){
            throw new RuntimeException(e);
        }
       return publicKey;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.tokenStore(tokenStore());
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(
                jwtAccessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter()  {
        var converter = new JwtAccessTokenConverter();
        converter.setVerifierKey( publicKey());
        return converter;
    }
}

```
Now all resource endpoints require an authentication token, which requires a user to be authenticated
This fulfills the following requirement  
* An user can be authenticated only if he previously registered in the system with a username and a password*

### add logic to implement 'Only the authenticated user can add a profile for themselves'. 
enable method level security
```
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ProjectConfig {
} 
```
Add a @PreAuthorize annotation to compare the profile username and the authentication principal
```
public class HealthProfileService {
...
  @PreAuthorize("#profile.username == authentication.principal")
  public void addHealthProfile(HealthProfile profile) {
```

#### Validate that another user can't add a profile for a different username than its self.
setup postman requests to perform the following
- request an access token for 'alice' who doesn't have a profile yet 
- POST an alice profile with an alice token -> success
- POST a  bob   profile with an alice token -> authentication failure


 
 
###  Endpoint used to get details about a health profile by providing the username as an input value (GET /profile/{username}):
*   **Security requirement:** 
- A user can get the details of their own health profile. 
- An admin can get the details of any profile. 
- A non-admin user cannot get details of other users’ profiles. 
- An admin user is a user having authority “admin”.
#### implement method security 
```
public class HealthProfileService {
 
  @PreAuthorize("#username == authentication.principal or hasAuthority('ROLE_ADMIN')")
  public HealthProfile findHealthProfile(String username) {
```

#### Add postman tests

- password grant token for alice
- find alice profile as alice e-> success
- find bob profile as alice -> auth fail
- password grant token for admin
- find alice profile as admin -> success
- find bob profile as admin ->  success

###  Endpoint that the client can call to delete a specific health profile by providing the username as an input (DELETE /profile/{username}).
*   **Security requirement:** 
- Only an admin can call the DELETE /profile/username endpoint. 
- An admin user is a user having authority “admin”.

#### Implement method security on delete method

```
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public void deleteHealthProfile(String username) {
```
#### Add Postman tests
- on oauth server get a password grant token for alice
- on resource server attempt to delete /profile/bob as alice ->fail
- on resource server attempt to delete /profile/alice as alice ->fail

- on oauth server get a password grant token for admin
- on resource server attempt create a new /profile/xyz as admin -> success
- on resource server attempt to delete /profile/xyz as admin ->success
 


###  Endpoint that the client can call to add a new health metric record (POST /metric). 
*   **Security requirement:** A health metric record can only be added for the authenticated user.

Add the @PreAuthorize method security 
```
@PreAuthorize("#healthMetric.profile.username == authentication.principal")
  public void addHealthMetric(HealthMetric healthMetric) {
```
Add Postman tests 
- get a token for john
- post a metric for jane user -> auth fail
- post a metric for john user -> success

###  Endpoint that the client can call to retrieve the health metrics history of a user (GET /metric/{username}). 
*   **Security requirement:** The client can get the health metric records only for the authenticated user.


###  Endpoint that the client can call to delete the metric history of a user (DELETE /metric/username).
*   **Security requirement:** Only an admin user can call this endpoint to delete the history of any user of the app.
###  Endpoint that a system can call to send a list of health advice. A health advice has the username and a text description of the advice (POST /advice).
*   **Security requirement:** Someone can call this endpoint using a token generated with the client credentials grant type if they have the scope “advice”.
