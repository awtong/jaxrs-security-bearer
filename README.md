## Synopsis

Generic JAX-RS authentication framework.

## Code Example

* [Register SecurityFeature](https://docs.oracle.com/javaee/7/api/javax/ws/rs/core/Application.html#getClasses) with your JAX-RS application.
* Annotate any JAX-RS endpoint you want secured with @Secured.
     * scheme will be the authorization scheme (ex: Basic, Bearer)
     * authenticator will be the code execute when the aforementioned scheme is specified in the Authorization request header.
     * realm is 

## Tests

Go to the root directory of the project and run

    ./gradlew test

The output will be located in build\reports\tests\test


## Resoures

* [RFC 2616 - Authorization Header](https://tools.ietf.org/html/rfc2616#section-14.8) 
* [Basic authentication](https://en.wikipedia.org/wiki/Basic_access_authentication)

## License

[Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0.txt)