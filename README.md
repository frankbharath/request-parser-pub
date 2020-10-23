# Rentpal

This document describes the purpose of developing rentpal, tools, and technologies that were used to develop the project.

## Idea behind the project
The inception of the computer has revolutionized the way we approach a problem. An example could be, as a student in a foreign country, I had to renew my visa. The process was not easy as I had to get an appointment which was very tough to book. After having the appointment, I had to make sure that I have all the documents with me. Unfortunately, I had a document missing that denied my visa and I had to wait for another 45 days to book an appointment.

But fortunately, now everything has moved online, all I had to do is create an online account, apply for the visa, and upload all the necessary documents. Even if there is a document missing I will get a message from the visa office, asking me to upload the file. This process is fairly simple when compared to the previous approach.

Even though there is technology advancement in a lot of areas, there are few areas where we could do better with existing technologies. One of the areas is property management.

We brainstormed with a lot of ideas of how we could approach this problem and below are the initial features that we wanted to implement.

* The ability of a property owner to manage the properties. As of now, a property could be either an apartment building or a single house - development completed.
* The ability to share the property with a tenant based on availability - development completed.
* The ability to send a digital rental agreement to the tenant - development completed.
* The ability to receive rent from the tenants and send monthly rent receipts automatically - under development.
* The ability to provide a messaging service between an owner and tenant - under development. 

## Features 

### 1. Property management
  - As of now, an owner can add a property that could be either an apartment building or a single house. Below are the attributes that we gather to store the property information
    * Address
    * Rent
    * Area
    * Capacity
    * Floor number and door number(applicable for apartment building)
    
    [![addproperty.png](https://i.postimg.cc/QtSFPW4p/addproperty.png)](https://postimg.cc/2bbjL3f6)
 
  - Above screenshot is a cite universite property of apartment type and its corresponding attributes. An owner can add any number of properties and there is no threshold. 
 
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
 
 
 
    
  
  

