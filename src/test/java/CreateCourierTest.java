import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class CreateCourierTest {

    @Rule
    public TestName testName = new TestName();

    @Test
    public void createNewCourierTest(){
        Response response = sendPostRequestCourier("kotov_35", "qwe1234", "Maxim");
        compareStatusCode201(response);
        compareBodyForOkTrue(response);
    }

    @Test
    public void cantCreateTwoSameCourierTest(){
        sendPostRequestCourier("kotov_35", "qwe1234", "Maxim");
        Response response = sendPostRequestCourier("kotov_35", "qwe1234", "Maxim");
        compareStatusCode409(response);
        compareMessageBodyForLoginAlreadyUse(response);
    }

    @Test
    public void cantCreateCourierWithOutLoginTest(){
        Response response = sendPostRequestCourier("", "qwe1234", "Maxim");
        compareStatusCode400(response);
        compareMessageBodyForNotEnoughData(response);
    }

    @Test
    public void cantCreateCourierWithoutPasswordTest(){
        Response response = sendPostRequestCourier("kotov_35", "", "Maxim");
        compareStatusCode400(response);
        compareMessageBodyForNotEnoughData(response);
    }

    @Test
    public void cantCreateCourierWithoutLoginAndPasswordTest(){
        Response response = sendPostRequestCourier("", "", "Maxim");
        compareStatusCode400(response);
        compareMessageBodyForNotEnoughData(response);
    }

    @Test
    public void createCourierWithoutFirstName(){
        Response response = sendPostRequestCourier("kotov_35", "qwe1234", "");
        compareStatusCode201(response);
        compareBodyForOkTrue(response);
    }

    @After //логин для получения айди + удаление созданной учетной записи с фильтрацией по названию кейса только при успешных созданиях курьера
    public void deleteNewCourier(){
        if(testName.getMethodName().equals("createNewCourierTest") || testName.getMethodName().equals("cantCreateTwoSameCourierTest") || testName.getMethodName().equals("createCourierWithoutFirstName")) {
            LoginCourierApi loginCourierApi = new LoginCourierApi();
            LoginCourier loginCourier = new LoginCourier("kotov_35", "qwe1234");
            Response response = loginCourierApi.postLoginCourierRequest(loginCourier);
            int id = response.then().extract().path("id");

            DeleteCourierApi deleteCourierApi = new DeleteCourierApi();
            deleteCourierApi.deleteCourierRequest(loginCourier, id);
        }
    }

    @Step("Send POST request to /api/v1/courier")
    public Response sendPostRequestCourier(String login, String password, String firstName) {
        CreateCourierApi createCourierApi = new CreateCourierApi();
        CreateCourier createCourier = new CreateCourier(login, password, firstName);
        Response response = createCourierApi.postCreateCourierRequest(createCourier);
        return response;
    }

    @Step("Compare statusCode 201")
    public void compareStatusCode201(Response response){
        response.then().assertThat().statusCode(SC_CREATED);
    }

    @Step("Compare body for ok: true")
    public void compareBodyForOkTrue(Response response){
        response.then().assertThat().body("ok", equalTo(true));
    }

    @Step("Compare statusCode 409")
    public void compareStatusCode409(Response response){
        response.then().assertThat().statusCode(SC_CONFLICT);
    }

    @Step("Compare body message for Этот логин уже используется. Попробуйте другой")
    public void compareMessageBodyForLoginAlreadyUse(Response response){
        response.then().assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }


    @Step("Compare statusCode 400")
    public void compareStatusCode400(Response response){
        response.then().assertThat().statusCode(SC_BAD_REQUEST);
    }

    @Step("Compare body message for Недостаточно данных для создания учетной записи")
    public void compareMessageBodyForNotEnoughData(Response response){
        response.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
}