import io.restassured.response.Response;

public class MakeOrdersApi extends BaseHttpClient{

    public Response postMakeOrdersRequest(MakeOrders makeOrders){
        return doPostRequest(URL.MAKE_ORDERS_API, makeOrders);
    }
}
