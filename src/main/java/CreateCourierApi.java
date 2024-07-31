import io.restassured.response.Response;

public class CreateCourierApi extends BaseHttpClient{

    public Response postCreateCourierRequest(CreateCourier createCourier){
        return doPostRequest(URL.COURIER_API, createCourier);
    }
}
