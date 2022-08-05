Watch store e-commerce backend
==============================

## Intro

The aim of this project is to develop REST API endpoints for the backend of an e-commerce store selling watches.
The application will be developed in Java using Spring Boot to speed up the development and configuration. The internal architecture will focus on creating models, controllers, services and repositories for the 2 main aspects of the requested API, the watch products and the order being processed at checkout. 

For the persistence aspect of the backend I have used an in-memory H2 database which is configured with the existing watch products from the project brief. In a real world scenario an alternative such as MySQL or even an NoSQL solution such as MongoDB could be configured for production. 

## Technology Stack
*	Java
*	Spring Boot
*	Spring-data-jpa
*       Maven
*       H2 database

## Installation

*	Install Java and ensure JAVA_HOME is set
* The compiled jar is available in the target folder so no need to build the project
* The project can be built with Maven wrapper allowing tests to be run without installing Maven
* For the purposes of thsi small demo a H2 in memory database will be used. 
  Under real world conditions a SQL database would need to be setup and configured

## Running tests

* There are 2 types of tests available, unit tests and an integration test
* A seperate profile has been created for the integration test so that the 2 sets of tests can be run independently
* Maven wrapper is used to build the project and run the tests as below (mvnw or mvnw.cmd on Windows)

```
  # To run the unit tests use:
  mvnw clean test
  
  # To run the integration test use:
  mvnw clean integration-test -Pfailsafe
```

## Running the application

* To launch the app from the pre-compiled jar you can use the commands as below
* The API endpoints can then be called to test the functionality 
* The below example uses curl to send the request, other tools such as Postman can also be used

```
  # From the main application folder the compiled jar can be launched
  java -jar ./target/dgault-ecommerce-backend-0.0.1-SNAPSHOT.jar

  # A POST request can then be sent to the API endpoint and the response displayed
  curl --request POST -H "Accept: application/json" 'http://localhost:8080/api/orders/checkout' --header 'Content-Type: application/json' --data-raw '[1, 2, 3, 2]'
```

## API Documentation

* The API endpoints are documented as below with the focus being on the requested checkout endpoint

### 1. Order-Api

  **POST** ``` /api/orders/checkout ```
  
  Takes a list of watch product ID's 
  ```
  [1, 2, 1, 4, 3, 2]
  ```
  Returns the total price of the order taking into account any discounts applied
  ```
  {
    "price":200.0
  }
  ```
  **GET** ``` /api/orders/ ```
  
  Returns a list of the orders currently stored
  
  **POST** ``` /api/orders/ ```
  
  Creates an order using the below and returns the new order object:
  ```
  {
    "watchOrders":[
      {"watch":{
        "id":3,
        "name":"Swatch",
        "price":50.0,
        "discount":null},
      "quantity":2}
    ]
  }
  ```

### 2. Watch-Api

	**GET** ``` /api/watches/ ```
  
  Returns a list of the watches currently stored. The return values should look as below:
  ```
  [{
    "id":1,
    "name":"Rolex",
    "price":100.0,
    "discount":{
        "displayMessage":"3 for 200",
        "type":"MULTI_BUY",
        "multiBuyQty":3,
        "multiBuyPrice":200.0}},
   {"id":2,
    "name":"Michael Kors",
    "price":80.0,
    "discount":{
        "displayMessage":"2 for 120",
        "type":"MULTI_BUY",
        "multiBuyQty":2,
        "multiBuyPrice":120.0}},
   {"id":3,
    "name":"Swatch",
    "price":50.0,
    "discount":null},
   {"id":4,
    "name":"Casio",
    "price":30.0,
    "discount":null}]
  ```

  
# Notes and limitations

#### DB Initialisation
Currently uses an in-memory H2 database, the initialisation is handled via CommandLineRunner bean to pre populate the database with existing watch products. In a real world scenario this would be better handled with a different persistent database solution, populated through a SQL script located under resources or alternative database migration tools such as Liquibase. A NoSQL database would also an option and the Spring Boot architecture would also allow for that possibility

#### CI/CD
Though CI/CD would have been out of scope for the project, ideally given more time I would have liked to have intoruced some more detailed configuration options as well as automating the testing via GitHub actions and introducing docker containers to show the   

#### Elements deemed out of scope
Altough a big part of modern dat e-commerce solutions both search and authentication and security concerns did not come into consideration for this small project


