import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.Arrays;
import java.util.List;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class MakeOrdersTest {

    String color;

    public MakeOrdersTest(String color){
        this.color = color;
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = URL.PLACEHOLDER_HOST;
    }

    @Parameterized.Parameters
    public static Object[][] testData() {
        return new Object[][]{
                {"BLACK"},
                {"GREY"},
                {""}
        };
    }

//    @Test
//    public void makeOrdersWithParamTest(){
//        List<String> colorsList = Arrays.asList(color);
//        MakeOrders makeOrders = new MakeOrders("Maxim", "Kotov", "Srednerogatskay st", "4", "+79991112233", 5, "2024-10-17", "Faster pls", colorsList);
//                given()
//                .header("Content-type", "application/json")
//                .body(makeOrders)
//                .when()
//                .post(URL.MAKE_ORDERS_API)
//                .then().assertThat().statusCode(equalTo(201))
//                .assertThat().body("track", notNullValue());
//    }

    @Test
    public void makeOrdersTest(){
        Response response = sendPostRequestOrders();
        compareStatusCode201(response);
        compareBodyTrackNotNullVallue(response);
    }


    @Step("Send POST request /api/v1/orders")
    public Response sendPostRequestOrders(){
        List<String> colorsList = Arrays.asList(color);
        MakeOrders makeOrders = new MakeOrders("Maxim", "Kotov", "Srednerogatskay st", "4", "+79991112233", 5, "2024-10-17", "Faster pls", colorsList);
        Response response = given()
                .header("Content-type", "application/json")
                .body(makeOrders)
                .when()
                .post(URL.MAKE_ORDERS_API);
        return response;
    }

    @Step("Compare statusCode 201")
    public void compareStatusCode201(Response response){
        response.then().assertThat().statusCode(equalTo(201));
    }

    @Step("Compare body track notNullVallue")
    public void compareBodyTrackNotNullVallue(Response response){
        response.then().assertThat().body("track", notNullValue());
    }
}
