import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.assertj.core.api.SoftAssertions;

import static io.restassured.RestAssured.given;

public class ApiSteps {

    @Step("Регистрация пользователя с именем: {username}, email: {email}")
    public void registerUser(String username, String email, String password, SoftAssertions softAssertions) {
        Response response = given()
                .contentType("application/x-www-form-urlencoded")
                .formParam("username", username)
                .formParam("email", email)
                .formParam("password", password)
                .when()
                .post("/user/create");

        int statusCode = response.statusCode();
        String message = response.jsonPath().getString("message");

        // Используем мягкие ассерты, переданные в метод
        softAssertions.assertThat(statusCode).isEqualTo(201);
        softAssertions.assertThat(message).isEqualTo("User created successfully");
    }

    @Step("Регистрация пользователя с существующим email: {email}")
    public void registerUserWithExistingEmail(String username, String email, String password, SoftAssertions softAssertions) {
        Response response = given()
                .contentType("application/x-www-form-urlencoded")
                .formParam("username", username)
                .formParam("email", email)
                .formParam("password", password)
                .when()
                .post("/user/create");

        int statusCode = response.statusCode();
        String message = response.jsonPath().getString("message");

        softAssertions.assertThat(statusCode).isEqualTo(409);
        softAssertions.assertThat(message).isEqualTo("Email already in use");
    }

    @Step("Регистрация пользователя с пустыми полями")
    public void registerUserWithEmptyFields(SoftAssertions softAssertions) {
        Response response = given()
                .contentType("application/x-www-form-urlencoded")
                .formParam("username", "")
                .formParam("email", "")
                .formParam("password", "")
                .when()
                .post("/user/create");

        int statusCode = response.statusCode();
        String usernameError = response.jsonPath().getString("errors.username");
        String emailError = response.jsonPath().getString("errors.email");
        String passwordError = response.jsonPath().getString("errors.password");

        softAssertions.assertThat(statusCode).isEqualTo(400);
        softAssertions.assertThat(usernameError).isEqualTo("Username cannot be empty");
        softAssertions.assertThat(emailError).isEqualTo("Email cannot be empty");
        softAssertions.assertThat(passwordError).isEqualTo("Password cannot be empty");
    }

    @Step("Регистрация пользователя с некорректным email: {email}")
    public void registerUserWithInvalidEmail(String username, String email, String password, SoftAssertions softAssertions) {
        Response response = given()
                .contentType("application/x-www-form-urlencoded")
                .formParam("username", username)
                .formParam("email", email)
                .formParam("password", password)
                .when()
                .post("/user/create");

        int statusCode = response.statusCode();
        String message = response.jsonPath().getString("message");

        softAssertions.assertThat(statusCode).isEqualTo(400);
        softAssertions.assertThat(message).isEqualTo("Invalid email format");
    }

    @Step("Получение списка всех пользователей")
    public void getAllUsers(SoftAssertions softAssertions) {
        Response response = given()
                .when()
                .get("/user/get");

        int statusCode = response.statusCode();
        int usersCount = response.jsonPath().getList("$").size();

        softAssertions.assertThat(statusCode).isEqualTo(200);
        softAssertions.assertThat(usersCount).isGreaterThan(0); // Предполагается, что есть пользователи
    }

    @Step("Проверка получения пустого списка пользователей")
    public void getNoUsers(SoftAssertions softAssertions) {
        Response response = given()
                .when()
                .get("/user/get");

        int statusCode = response.statusCode();
        int usersCount = response.jsonPath().getList("$").size();

        softAssertions.assertThat(statusCode).isEqualTo(200);
        softAssertions.assertThat(usersCount).isEqualTo(0); // Предполагается, что нет пользователей
    }
}
