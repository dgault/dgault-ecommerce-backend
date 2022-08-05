Watch store e-commerce backend
==============================

## Intro

The aim of this project is to develop REST API endpoints for the backend of an e-commerce store selling watches.
The application will be developed in Java using Spring Boot to speed up the development and configuration. The internal architecture will focus on creating models, controllers and services for the 2 main aspects of the requested API, the watch products and the order being processed at checkout. 

For the persistence aspect of the backend I will aim to provide the ability to configure different database solutions. For the purposes of this short demo project an in memory database will be used for testing purposes and as a proof on concept. In a real world scenario an alternative such as MySQL or even an NoSQL solution such as MongoDB could be configured for production. 

## Technology Stack
* Java
* Spring Boot
* Spring-data-jpa
* Maven
* MySQL

## Installation

Installation instructions to follow

## Running tests

Testing instructions to follow

## Running the application

Build and run application instructions to follow

## API Documentation
* The API endpoints are documented as below with the focus being on the requested checkout endpoint

  **POST** ``` /checkout ```
  
  Takes a list of watch product ID's and returns the total price taking into account any discounts applied

  
# Notes and limitations


