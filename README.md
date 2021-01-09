# Request Parser

The document describes the purpose of developing a framework that validates incoming parameter requests.

## Problem
When I was working on my personal project, I had to make sure all the incoming request parameters are validated before providing them to corresponding service class. I was developing my project using Spring framework and spring does provide annotations to validate the request parameters. For example 

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
- We can take inspiration from Windows file explorer. In the left pane of file explorer, we have folders and if we click on the arrow button on folders, it shows child folder and we can further click and go on. We have some form tree structure where each folder is root to its children folder and documents. We can use the same technique to convert our rules into a trie tree structure.

- We can convert our example into an trie tree structure.
![urltree](https://user-images.githubusercontent.com/49817583/97086256-6ab6dc00-1622-11eb-85dc-6d3d0a125742.png)

The above tree represents,

- /api/user/{id} – The rules pertaining to POST, PUT and DELETE will be stored in user node. The rule for GET user will stored in {id} node  
- /api/property/apartment/{id} - The rules pertaining to POST, PUT and DELETE will be stored in apartment node. The rule for GET apartment will stored in {id} node  
- /api/property/house/{id} - The rules pertaining to POST, PUT and DELETE will be stored in house node. The rule for GET house will stored in {id} node  
- /api/tenant/{id} - The rules pertaining to POST, PUT and DELETE will be stored in tenant node. The rule for GET tenant will stored in {id} node  
- /api/tenant/lease/{id} - POST, PUT, DELETE, GET - The rules pertaining to POST, PUT and DELETE will be stored in lease node. The rule for GET lease will stored in {id} node  

We can store the tree in main memory. Once when the request hits the server, we can parse the tree and find the corresponding rule for the request.

### Supported Request Types
As of now this library supports application/x-www-form-urlencoded and multipart/form-data

### Supported Data Types
A request parameter value can be either a string, list or object. 
Example,
- email=test@test.com&password=test@123
  - email and password can be categorized as string
- id=1&id=2&id=3, 
  - id be categorized as list of string. 
- address={line_1:10B, line_2: Camille Groult,code:94400,city:paris},
  - sometimes we might be sending JSONObject to server. A list of objects can be sent from client as well. 

These can represented in XML as
![sampleurl](https://user-images.githubusercontent.com/49817583/97086559-3f34f100-1624-11eb-9bb2-6751dfba9ddd.png)

But in the end every parameter should have parameter rule associate with it. Moreover, an object can contain another object as well. But the depth of the object is restricted to 3 as of now.
