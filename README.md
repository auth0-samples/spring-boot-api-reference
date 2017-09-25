
## API Sample using Spring Boot

Demonstration app, illustrates how change password endpoint may be implemented,
requiring APIv2 management token & secured by JWT Access token with `update:password` scope.

## Setup and Configuration

Before starting you must upate auth0.properties file located in `src/main/resources 

```
auth0.domain:{tenant}.auth0.com
auth0.clientId: 123
auth0.clientSecret: asdfadsf
auth0.apiAudience: https://myapi
```

The `auth0.apiAudience` will be https://your-domain/ (the trailing slash is necessary) and the `api-audience` will be the unique identifer set when creating the API.

## Prerequisites

Before starting this application runs using Maven.  To use this sample as is you must install Maven.  I recommend installing Maven through [Home Brew](http://brew.sh).  Once you've installed Home Brew you can run the following command to install Maven.

```sh
brew install maven
```

To ensure Maven has been successfully installed run the following command to check the version.  For this sample to work properly Maven version 3.0 or later is required.

```sh
mvn -v
```

## Configuring the API in Auth0

Before anyone can use this API with Auth0 it must be created as an API using the Auth0 Management Dashboard.  From the dashboard navigate the the API section and click on the Create API button in the top right.  

## Build and Run

Once you've setup Maven and configured your application.  You can run the application with the following command.  This, by default, runs on port 3001.  If you want to change the port you can modify `/src/resources/application.properties` to use any port you'd prefer.

```sh
mvn spring-boot:run
```

## Test the API

