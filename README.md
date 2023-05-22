
# PayPal Service Demo

This project is a Demo to introduce, how you can access PayPal payment with PayPal core.


## Getting Started
These instructions will help you get a copy of the project and running it on your local machine for development and testing purposes.



## How to Run
This application requires pre-installed [JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) or higher. [See more.](https://www.oracle.com/java/technologies/downloads/#jdk19-windows)
* Clone this repository
```
git clone https://github.com/SEM24/PayPal-Checkout-Demo.git
```
* Make sure you are using JDK 17 and Maven
* You can build the project and run the tests by running ```./mvnw clean package```
* For the first time, it will download and install Maven version configured in the project settings.

* After build, the folder /target will be created with a compiled .jar ready to be launched.

### Run Spring Boot app using Maven:
Now you can launch the server(default port is 8080).

* Run Spring Boot app using Maven: ```mvn spring-boot:run```

### Built With:
* Maven - Build tool
* Spring Boot - Web framework
* PayPal Core

## Explanation of Requests
POST http://localhost:8080/paypal/checkout?sum=10
* This Request Method requires "sum" (the amount of money to pay). Origin in headers(to find the way to redirect, helps to work with frontend).

POST http://localhost:8080/paypal/capture?token=YOUR_TOKEN&PayerID=YOUR_PAYER_ID 
* This Request Method requires paresed token from URL that you get after success order creation. token - order_id of your payment, PayerID - id of your paypal account.
Request examples can be found in the project core.
