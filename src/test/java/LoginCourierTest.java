import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class LoginCourierTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = URL.PLACEHOLDER_HOST;

        CreateCourier createCourier = new CreateCourier("kotov_35", "qwe1234", "Maxim");
        given()
                .header("Content-type", "application/json")
                .body(createCourier)
                .when()
                .post(URL.CREATE_COURIER_API)
                .then().assertThat().statusCode(201)
                .and()
                .assertThat().body("ok", equalTo(true));
    }

//    @Test //курьер может авторизоваться, для авторизации нужно передать все обязательные поля, успешный запрос возвращает id
//    public void loginCourierTest() {
//        LoginCourier loginCourier = new LoginCourier("kotov_35", "qwe1234");
//        given()
//                .header("Content-type", "application/json")
//                .body(loginCourier)
//                .when()
//                .post(URL.LOGIN_COURIER_API)
//                .then().assertThat().statusCode(200)
//                .and()
//                .assertThat().body("id", notNullValue());
//    }

    @Test
    public void loginCourierTest(){
        Response response = sendPostRequestCourierLogin("kotov_35", "qwe1234");
        compareStatusCode200(response);
        compareBodyIdForNotNullVallue(response);
    }

//    @Test //система вернёт ошибку, если неправильно указать логин
//    public void loginCourierWithWrongLoginTest(){
//        LoginCourier loginCourier = new LoginCourier("kotov_999", "qwe1234");
//        given()
//                .header("Content-type", "application/json")
//                .body(loginCourier)
//                .when()
//                .post(URL.LOGIN_COURIER_API)
//                .then().assertThat().statusCode(404)
//                .and()
//                .assertThat().body("message", equalTo("Учетная запись не найдена"));
//    }

    @Test
    public void loginCourierWithWrongLoginTest(){
        Response response = sendPostRequestCourierLogin("kotov_999", "qwe1234");
        compareStatusCode404(response);
        compareBodyMessageAccountNotFound(response);
    }

//    @Test //система вернёт ошибку, если неправильно указать пароль
//    public void loginCourierWithWrongPasswordTest(){
//        LoginCourier loginCourier = new LoginCourier("kotov_35", "55qwe55");
//        given()
//                .header("Content-type", "application/json")
//                .body(loginCourier)
//                .when()
//                .post(URL.LOGIN_COURIER_API)
//                .then().assertThat().statusCode(404)
//                .and()
//                .assertThat().body("message", equalTo("Учетная запись не найдена"));
//    }

    @Test
    public void loginCourierWithWrongPasswordTest(){
        Response response = sendPostRequestCourierLogin("kotov_35", "55qwe55");
        compareStatusCode404(response);
        compareBodyMessageAccountNotFound(response);
    }

//    @Test //если логина нет, запрос возвращает ошибку
//    public void loginCourierWithoutLoginTest(){
//        LoginCourier loginCourier = new LoginCourier("", "qwe1234");
//        given()
//                .header("Content-type", "application/json")
//                .body(loginCourier)
//                .when()
//                .post(URL.LOGIN_COURIER_API)
//                .then().assertThat().statusCode(400)
//                .and()
//                .assertThat().body("message", equalTo("Недостаточно данных для входа"));
//    }

    @Test
    public void loginCourierWithoutLoginTest(){
        Response response = sendPostRequestCourierLogin("", "qwe1234");
        compareStatusCode400(response);
        compareBodyMessageNotEnoughDataToLogIn(response);
    }

//    @Test //если пароля нет, запрос возвращает ошибку
//    public void loginCourierWithoutPasswordTest(){
//        LoginCourier loginCourier = new LoginCourier("kotov_35", "");
//        given()
//                .header("Content-type", "application/json")
//                .body(loginCourier)
//                .when()
//                .post(URL.LOGIN_COURIER_API)
//                .then().assertThat().statusCode(400)
//                .and()
//                .assertThat().body("message", equalTo("Недостаточно данных для входа"));
//    }

    @Test
    public void loginCourierWithoutPasswordTest(){
        Response response = sendPostRequestCourierLogin("kotov_35", "");
        compareStatusCode400(response);
        compareBodyMessageNotEnoughDataToLogIn(response);
    }

//    @Test //если авторизоваться под несуществующим пользователем, запрос возвращает ошибку;
//    public void loginCourierNonExistentUserTest(){
//        LoginCourier loginCourier = new LoginCourier("kotov_praktikum_999", "testdata12345");
//        given()
//                .header("Content-type", "application/json")
//                .body(loginCourier)
//                .when()
//                .post(URL.LOGIN_COURIER_API)
//                .then().assertThat().statusCode(404)
//                .and()
//                .assertThat().body("message", equalTo("Учетная запись не найдена"));
//    }

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
        LoginCourier loginCourier = new LoginCourier("kotov_35", "qwe1234");
        int response = given()
                .header("Content-type", "application/json")
                .body(loginCourier)
                .when()
                .post(URL.LOGIN_COURIER_API)
                .then().extract().path("id");

        given()
                .header("Content-type", "application/json")
                .when()
                .delete("/api/v1/courier/" + response)
                .then().assertThat().statusCode(200);
    }

    @Step("Send POST request /api/v1/courier/login")
    public Response sendPostRequestCourierLogin(String login, String password){
        LoginCourier loginCourier = new LoginCourier(login, password);
        Response response = given()
                .header("Content-type", "application/json")
                .body(loginCourier)
                .when()
                .post(URL.LOGIN_COURIER_API);
        return response;
    }

    @Step("Compare statusCode 200")
    public void compareStatusCode200(Response response){
        response.then().assertThat().statusCode(200);
    }

    @Step("Compare body id for notNullVallue")
    public void compareBodyIdForNotNullVallue(Response response){
        response.then().assertThat().body("id", notNullValue());
    }

    @Step("Compare statusCode 404")
    public void compareStatusCode404(Response response){
        response.then().assertThat().statusCode(404);
    }

    @Step("Compare body message Учетная запись не найдена")
    public void compareBodyMessageAccountNotFound(Response response){
        response.then().assertThat().body("message", equalTo("Учетная запись не найдена"));
    }

    @Step("Compare statusCode 400")
    public void compareStatusCode400(Response response){
        response.then().assertThat().statusCode(400);
    }

    @Step("Compare body message Недостаточно данных для входа")
    public void compareBodyMessageNotEnoughDataToLogIn(Response response){
        response.then().assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }
}