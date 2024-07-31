import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class LoginCourierTest {

    @Before
    public void setUp() {
        CreateCourierApi createCourierApi = new CreateCourierApi();
        CreateCourier createCourier = new CreateCourier("kotov_35", "qwe1234", "Maxim");
        createCourierApi.postCreateCourierRequest(createCourier);
    }

    @Test
    public void loginCourierTest(){
        Response response = sendPostRequestCourierLogin("kotov_35", "qwe1234");
        compareStatusCode200(response);
        compareBodyIdForNotNullVallue(response);
    }

    @Test
    public void loginCourierWithWrongLoginTest(){
        Response response = sendPostRequestCourierLogin("kotov_999", "qwe1234");
        compareStatusCode404(response);
        compareBodyMessageAccountNotFound(response);
    }

    @Test
    public void loginCourierWithWrongPasswordTest(){
        Response response = sendPostRequestCourierLogin("kotov_35", "55qwe55");
        compareStatusCode404(response);
        compareBodyMessageAccountNotFound(response);
    }

    @Test
    public void loginCourierWithoutLoginTest(){
        Response response = sendPostRequestCourierLogin("", "qwe1234");
        compareStatusCode400(response);
        compareBodyMessageNotEnoughDataToLogIn(response);
    }

    @Test
    public void loginCourierWithoutPasswordTest(){
        Response response = sendPostRequestCourierLogin("kotov_35", "");
        compareStatusCode400(response);
        compareBodyMessageNotEnoughDataToLogIn(response);
    }

    @Test
    public void loginCourierNonExistentUserTest(){
        Response response = sendPostRequestCourierLogin("kotov_999", "55qwe55");
        compareStatusCode404(response);
        compareBodyMessageAccountNotFound(response);
    }

    @Test
    public void loginCourierWithoutLoginAndPasswordTest(){
        Response response = sendPostRequestCourierLogin("", "");
        compareStatusCode400(response);
        compareBodyMessageNotEnoughDataToLogIn(response);
    }


    @After
    public void deleteNewCourier(){
        LoginCourierApi loginCourierApi = new LoginCourierApi();
        LoginCourier loginCourier = new LoginCourier("kotov_35", "qwe1234");
        Response response = loginCourierApi.postLoginCourierRequest(loginCourier);
        int id = response.then().extract().path("id");

        DeleteCourierApi deleteCourierApi = new DeleteCourierApi();
        deleteCourierApi.deleteCourierRequest(loginCourier, id);
    }

    @Step("Send POST request /api/v1/courier/login")
    public Response sendPostRequestCourierLogin(String login, String password){
        LoginCourierApi loginCourierApi = new LoginCourierApi();
        LoginCourier loginCourier = new LoginCourier(login, password);
        Response response = loginCourierApi.postLoginCourierRequest(loginCourier);
        return response;
    }

    @Step("Compare statusCode 200")
    public void compareStatusCode200(Response response){
        response.then().assertThat().statusCode(SC_OK);
    }

    @Step("Compare body id for notNullVallue")
    public void compareBodyIdForNotNullVallue(Response response){
        response.then().assertThat().body("id", notNullValue());
    }

    @Step("Compare statusCode 404")
    public void compareStatusCode404(Response response){
        response.then().assertThat().statusCode(SC_NOT_FOUND);
    }

    @Step("Compare body message Учетная запись не найдена")
    public void compareBodyMessageAccountNotFound(Response response){
        response.then().assertThat().body("message", equalTo("Учетная запись не найдена"));
    }

    @Step("Compare statusCode 400")
    public void compareStatusCode400(Response response){
        response.then().assertThat().statusCode(SC_BAD_REQUEST);
    }

    @Step("Compare body message Недостаточно данных для входа")
    public void compareBodyMessageNotEnoughDataToLogIn(Response response){
        response.then().assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }
}