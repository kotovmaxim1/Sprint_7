import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.Test;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.notNullValue;

public class OrdersListTest {

    @Test
    public void getOrdersListTest(){
        Response response = sendGetRequestOrders();
        compareStatusCode200(response);
        compareBodyOrdersNotNullVallue(response);
    }

    @Step("Send GET request /api/v1/orders")
    public Response sendGetRequestOrders(){
        OrdersListApi ordersListApi = new OrdersListApi();
        Response response = ordersListApi.getOrderRequest();
        return response;
    }


    @Step("compare statusCode 200")
    public void compareStatusCode200(Response response){
        response.then().statusCode(SC_OK).and();
    }

    @Step("compare body orders notNullVallue")
    public void compareBodyOrdersNotNullVallue(Response response){
        response.then().assertThat().body("orders", notNullValue());
    }
}