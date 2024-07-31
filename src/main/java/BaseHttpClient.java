import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.ErrorLoggingFilter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public abstract class BaseHttpClient {

    private RequestSpecification baseRequestSpec(){
        return new RequestSpecBuilder()
                .setBaseUri(URL.PLACEHOLDER_HOST)
                .addHeader("Content-Type", "application/json")
                .setRelaxedHTTPSValidation()
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .addFilter(new ErrorLoggingFilter())
                .build();
    }

    protected Response doGetRequest(String path){
        return given()
                .spec(baseRequestSpec())
                .get(path)
                .thenReturn();
    }

    protected Response doPostRequest(String path, Object body){
        return given()
                .spec(baseRequestSpec())
                .body(body)
                .post(path)
                .thenReturn();
    }

    protected Response doDeleteRequest(String path, Object body, int id){
        return given()
                .spec(baseRequestSpec())
                .body(body)
                .delete(path + id)
                .thenReturn();
    }
}