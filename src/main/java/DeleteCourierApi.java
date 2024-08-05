import io.restassured.response.Response;

public class DeleteCourierApi extends BaseHttpClient {

    public Response deleteCourierRequest(LoginCourier loginCourier, int id){
        return doDeleteRequest(URL.COURIER_API, loginCourier, id);
    }
}
