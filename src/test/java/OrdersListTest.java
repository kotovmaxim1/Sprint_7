import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

public class OrdersListTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = URL.PLACEHOLDER_HOST;
    }

//    @Test
//    public void getOrdersListTest(){
//        given()
//                .header("Content-type", "application/json")
//                .when()
//                .get(URL.GET_ORDERS_API)
//                .then()
//                .statusCode(200)
//                .and()
//                .assertThat().body("orders", notNullValue());
//    }

    @Test
    public void getOrdersListTest(){
        Response response = sendGetRequestOrders();
        compareStatusCode200(response);
        compareBodyOrdersNotNullVallue(response);
    }

    @Step("Send GET request /api/v1/orders")
    public Response sendGetRequestOrders(){
        Response response = given()
                .header("Content-type", "application/json")
                .when()
                .get(URL.GET_ORDERS_API);
        return response;
    }

    @Step("compare statusCode 200")
    public void compareStatusCode200(Response response){
        response.then().statusCode(200).and();
    }

    @Step("compare body orders notNullVallue")
    public void compareBodyOrdersNotNullVallue(Response response){
        response.then().assertThat().body("orders", notNullValue());
    }
}
