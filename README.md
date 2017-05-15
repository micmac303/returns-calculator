# README #

# Summary #

* Application name: returns-calculator
* Version: 1.0-SNAPSHOT
* Author: Michael McCarthy

# Description #
Protoype application to satisfy HSBC JavaTest for Java/J2EE Developer role.

# Notes and Assumptions #
* The application has been written in order to satisfy only the
requirements specified in the test description
 
* An assumption has been made that clients would already exist as client creation
was not specified in the requirements, therefore a simple utility controller
has been implemented to add several clients to the database. See "Set up" below for instructions to populate the database with
some clients post start up.

* Deal interest values are stored in pence and converted to cents for
total calculations
 
* The value N for the simple interest calculation is derived 
from n (number of times per year that interest is applied)
multiplied by the number of years

* The exchange rate service from pounds to USD is assumed to already
exist as external service and has not been developed as part of this
solution. A placeholder service returning a hard coded value of 1.5 has been
implemented to illustrate how this external service would be integrated
and used.

### Set up ###

* To test and run the application execute the following from the command line:

mvn clean package && java -jar target/returns-calculator-1.0-SNAPSHOT.jar

* To populate the database with sample clients post start up:
 
Make a GET request to http://localhost:8080/populate 

### API details and sample requests and responses ###

* /populate [GET]
Utility method to populate the database with 3 clients

* /simple [POST]
Method to calculate, return and persist a Simple Interest Deal.

Sample Request JSON:

{
  	"clientId": 1,
  	"principal": 100000,
    "interestRate": "0.01",
    "timesApplied": 1,
    "years": 5
}

Sample Response JSON:

{
    "id": 1,
    "returnAmount": 5000,
        "client": {
            "id": 1
        }
}

* /compound [POST]
Method to calculate, return and persist a Compound Interest Deal.

Sample Request JSON:

{
  	"clientId": 1,
  	"principal": 100000,
    "interestRate": "0.01",
    "timesApplied": 1,
    "years": 5
}

Sample Response JSON:

{
    "id": 2,
    "returnAmount": 5101,
        "client": {
            "id": 1
        }
}

* /total [GET]
Method to calculate and return the total return across all deals for all clients.

Sample Response JSON;

{
    "total": 15151
}

