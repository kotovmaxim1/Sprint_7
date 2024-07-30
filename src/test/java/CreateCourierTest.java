import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class CreateCourierTest {

    @Rule
    public TestName testName = new TestName();

    @Before
    public void setUp() {
        RestAssured.baseURI = URL.PLACEHOLDER_HOST;
    }


//    @Test //курьера можно создать, успешный запрос возвращает ok: true, запрос возвращает правильный код ответа
//    public void createNewCourierTest(){
//        CreateCourier createCourier = new CreateCourier("kotov_35", "qwe1234", "Maxim");
//        given()
//                .header("Content-type", "application/json")
//                .body(createCourier)
//                .when()
//                .post(URL.CREATE_COURIER_API)
//                .then().assertThat().statusCode(201)
//                .and()
//                .assertThat().body("ok", equalTo(true));
//    }

    @Test
    public void createNewCourierTest(){
        Response response = sendPostRequestCourier("kotov_35", "qwe1234", "Maxim");
        compareStatusCode201(response);
        compareBodyForOkTrue(response);
    }

//    @Test //нельзя создать двух одинаковых курьеров, если создать пользователя с логином, который уже есть, возвращается ошибка, запрос возвращает правильный код ответа
//    public void cantCreateTwoSameCourierTest(){
//        CreateCourier createCourier = new CreateCourier("kotov_35", "qwe1234", "Maxim");
//        given()
//                .header("Content-type", "application/json")
//                .body(createCourier)
//                .when()
//                .post(URL.CREATE_COURIER_API)
//                .then().assertThat().statusCode(201)
//                .and()
//                .assertThat().body("ok", equalTo(true));
//        given()
//                .header("Content-type", "application/json")
//                .body(createCourier)
//                .when()
//                .post(URL.CREATE_COURIER_API)
//                .then().assertThat().statusCode(409)
//                .and()
//                .assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
//    }

    @Test
    public void cantCreateTwoSameCourierTest(){
        sendPostRequestCourier("kotov_35", "qwe1234", "Maxim");
        Response response = sendPostRequestCourier("kotov_35", "qwe1234", "Maxim");
        compareStatusCode409(response);
        compareMessageBodyForLoginAlreadyUse(response);
    }

//    @Test //если логина нет, запрос возвращает ошибку
//    public void cantCreateCourierWithoutLoginTest(){
//        CreateCourier createCourier = new CreateCourier("", "qwe1234", "Maxim");
//        given()
//                .header("Content-type", "application/json")
//                .body(createCourier)
//                .when()
//                .post(URL.CREATE_COURIER_API)
//                .then().assertThat().statusCode(400)
//                .and()
//                .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
//                .extract().statusCode();
//    }

    @Test
    public void cantCreateCourierWithOutLoginTest(){
        Response response = sendPostRequestCourier("", "qwe1234", "Maxim");
        compareStatusCode400(response);
        compareMessageBodyForNotEnoughData(response);
    }

//    @Test //если пароля нет, запрос возвращает ошибку
//    public void cantCreateCourierWithoutPasswordTest(){
//        CreateCourier createCourier = new CreateCourier("kotov_35", "", "Maxim");
//        given()
//                .header("Content-type", "application/json")
//                .body(createCourier)
//                .when()
//                .post(URL.CREATE_COURIER_API)
//                .then().assertThat().statusCode(400)
//                .and()
//                .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
//                .extract().statusCode();
//    }

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
            LoginCourier loginCourier = new LoginCourier("kotov_35", "qwe1234");
            int response = given()
                    .header("Content-type", "application/json")
                    .body(loginCourier)
                    .when()
                    .post("/api/v1/courier/login")
                    .then().extract().path("id");

            given()
                    .header("Content-type", "application/json")
                    .when()
                    .delete("/api/v1/courier/" + response)
                    .then().assertThat().statusCode(200);
        }
    }

    @Step("Send POST request to /api/v1/courier")
    public Response sendPostRequestCourier(String login, String password, String firstName) {
        CreateCourier createCourier = new CreateCourier(login, password, firstName);
        Response response = given()
                        .header("Content-type", "application/json")
                        .body(createCourier)
                        .when()
                        .post(URL.CREATE_COURIER_API);
        return response;
    }

    @Step("Compare statusCode 201")
    public void compareStatusCode201(Response response){
        response.then().assertThat().statusCode(201);
    }

    @Step("Compare body for ok: true")
    public void compareBodyForOkTrue(Response response){
        response.then().assertThat().body("ok", equalTo(true));
    }

    @Step("Compare statusCode 409")
    public void compareStatusCode409(Response response){
        response.then().assertThat().statusCode(409);
    }

    @Step("Compare body message for Этот логин уже используется. Попробуйте другой")
    public void compareMessageBodyForLoginAlreadyUse(Response response){
        response.then().assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }


    @Step("Compare statusCode 400")
    public void compareStatusCode400(Response response){
        response.then().assertThat().statusCode(400);
    }

    @Step("Compare body message for Недостаточно данных для создания учетной записи")
    public void compareMessageBodyForNotEnoughData(Response response){
        response.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
}