# Request Parser

The document describes the purpose of developing a framework that validates incoming parameter requests.

## Problem
When I was working on my project, I had to make sure all the incoming request parameters are validated before providing them to corresponding service class. I was developing my project using Spring framework and spring does provide annotations to validate the request parameters. For example 

@RestController
@Validated
public class TestController {<br/>
&nbsp;&nbsp;@GetMapping("/student/")<br/>
&nbsp;&nbsp;ResponseEntity<String> student(@RequestParam("age") @Min(5) int age) {<br/>
&nbsp;&nbsp;&nbsp;&nbsp;return ResponseEntity.ok("Your age is " + age);<br/>
&nbsp;&nbsp;}<br/>
}<br/>

The above student function expects the age value to be greater than or equal to 5 and throws exception if is less the 5. But what if there are multiple criteria’s such as minimum length, maximum length, required, pattern match? As my domain objects were complex, annotating each parameter with a validate functions such as Min, Max or Regex bloated my controller. I did not like it and I wanted to move the logic from controller to a single entity just like controller advice.  


## Solution 

As I know the request, I will define the rules based upon my expectations in an XML file. For example,

![url](https://user-images.githubusercontent.com/49817583/97086122-c339a980-1621-11eb-93fc-ccefd8450e4c.png)

Once when I have defined my rules in the XML file, I can read the file in two ways.
1.	Whenever a request hits my server, I can read the XML file and get URL rule for that request. If there is no URL then it is essentially like 404 error. If there exists a rule, I can parse the rule and create an object out of it. I can use the object to validate against my request parameters. But this approach is not efficient as it will be consuming to read XML file for each request. 
2.	A better approach would be to parse the file during the server startup and using some form of data structure we can store the rules in the memory. This is efficient than the above approach as we parse only one time.  

### What kind of data structure we can use to store the rules?
- We can take inspiration from Windows file explorer. In the left pane of file explorer, we have folders and if we click on the arrow button on folders, it shows child folder and we can further click and go on. We have some form tree structure where each folder is root to its children folder and documents. We can use the same technique to convert our rules into a n-ary tree structure.

- We can convert our example into an n-ary tree structure.
![urltree](https://user-images.githubusercontent.com/49817583/97086256-6ab6dc00-1622-11eb-85dc-6d3d0a125742.png)

The above tree represents,

- /api/user/{id} – The rules pertaining to POST, PUT and DELETE will be stored in user node. The rule for GET user will stored in {id} node  
- /api/property/apartment/{id} - The rules pertaining to POST, PUT and DELETE will be stored in apartment node. The rule for GET apartment will stored in {id} node  
- /api/property/house/{id} - The rules pertaining to POST, PUT and DELETE will be stored in house node. The rule for GET house will stored in {id} node  
- /api/tenant/{id} - The rules pertaining to POST, PUT and DELETE will be stored in tenant node. The rule for GET tenant will stored in {id} node  
- /api/tenant/lease/{id} - POST, PUT, DELETE, GET - The rules pertaining to POST, PUT and DELETE will be stored in lease node. The rule for GET lease will stored in {id} node  

We can store the tree in main memory. Once when the request hits the server, we can parse the tree and find the corresponding rule for the request.


 
 
### 2. Tenant Management
  - Once when an owner adds a property, the property is available for leasing. An owner can add a tenant and lease the property to the tenant according to the capacity at the time of leasing. 
  
    [![tenant.png](https://i.postimg.cc/pX3wNW67/tenant.png)](https://postimg.cc/Wh6Wgvj6)
    
  - After adding the tenant, an owner can share a property based on availability.
    
    [![lease.png](https://i.postimg.cc/Y05bP9Cz/lease.png)](https://postimg.cc/QKqQ9j5F)
    
### 3. Digital Rental Agreement
- The owner has an option to send a digital rental agreement if required. We are using Zoho Sign APIs to create a digital rental agreement and send it to the owner and tenant for e-signature.   
    
  [![digitalrentalagreement.png](https://i.postimg.cc/Z5pK2DV8/digitalrentalagreement.png)](https://postimg.cc/jDqbJvV2)
    
 - First, the owner receives the digital rental agreement and the tenant will receive the agreement after the owner e-signs the document.
    
  [![e-sign.png](https://i.postimg.cc/7Y6yStFH/e-sign.png)](https://postimg.cc/grf5WscQ)
  
### 4. Features in pipeline
- Integrate with Stripe for automatic rent payment and send rent receipts periodically.
- Provide a message service with a translator option.
- Move towards stateless authentication with JWT.

 ## Languages and frameworks used
 - Backend - Java 11 and Spring Framework
    - I chose to work with Java as I had prior experience. I wanted to learn a new Java framework and chose the Spring framework as it had many features such as spring-security, controller advice, Dependency Injection.
 - Frontend - Javascript ES6 and AngularJS Framework
     - I chose to work with Javascript as I had prior experience. I was looking for a lightweight framework and also the project is a single page application hence I chose to work with AngularJS
 - Unit testing - JUnit 5
 
 ## Database 
 - Postgres
    - I wanted to work with a database that has good support for indexing so that it could speed up the search query. Postgres had a lot of indexing such as partial, cardinality, GIN. I used GIN for a Full-Text search that avoids full table scan.
    
 ## Build Tool
 - Maven
 
 ## Repository
 - GitHub
  
 ## Devops
 - Jenkins - After finishing the project I wanted to deploy to the AWS server via Jenkins. I learnt Jenkins and built a pipeline script that will auto-deploy to my AWS server.
 - Docker - AWS server had Java version 1.7 but I built my project with Java 11. Even though I hardly used any features of Java 11, I had to make sure my project is compatible with the AWS server. So I created a docker image with Java 11 and pushed the image to the docker hub.
 - AWS - I chose to work with AWS because it had a lot of features and great support. I am using an EC2 instance to run my docker image. Amazon also provides ECR to run docker images but I had to stick with EC2 as it is available in the free tier. I am also using an RDS instance to run a Postgresql server. 
  
  [![buildstageview.png](https://i.postimg.cc/BbRhjGXb/buildstageview.png)](https://postimg.cc/XBcKPmK6)
 
 
 
    
  
  

