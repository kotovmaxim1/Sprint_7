import io.restassured.response.Response;

public class LoginCourierApi extends BaseHttpClient{

    public Response postLoginCourierRequest(LoginCourier loginCourier){
        return doPostRequest(URL.LOGIN_COURIER_API, loginCourier);
    }
}
