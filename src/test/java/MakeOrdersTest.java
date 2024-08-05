import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.Arrays;
import java.util.List;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class MakeOrdersTest {

    String color;

    public MakeOrdersTest(String color){
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[][] testData() {
        return new Object[][]{
                {"BLACK"},
                {"GREY"},
                {""},
                {"BLACK,GREY"}
        };
    }

    @Test
    public void makeOrdersTest(){
        Response response = sendPostRequestOrders();
        compareStatusCode201(response);
        compareBodyTrackNotNullVallue(response);
    }


    @Step("Send POST request /api/v1/orders")
    public Response sendPostRequestOrders(){
        MakeOrdersApi makeOrdersApi = new MakeOrdersApi();
        List<String> colorsList = Arrays.asList(color.split(","));
        MakeOrders makeOrders = new MakeOrders("Maxim", "Kotov", "Srednerogatskay st", "4", "+79991112233", 5, "2024-10-17", "Faster pls", colorsList);
        Response response = makeOrdersApi.postMakeOrdersRequest(makeOrders);
        return response;
    }

    @Step("Compare statusCode 201")
    public void compareStatusCode201(Response response){
        response.then().assertThat().statusCode(equalTo(SC_CREATED));
    }

    @Step("Compare body track notNullVallue")
    public void compareBodyTrackNotNullVallue(Response response){
        response.then().assertThat().body("track", notNullValue());
    }
}
