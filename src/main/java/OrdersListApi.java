import io.restassured.response.Response;

public class OrdersListApi extends BaseHttpClient{

    public Response getOrderRequest(){
        return doGetRequest(URL.GET_ORDERS_API);
    }
}
