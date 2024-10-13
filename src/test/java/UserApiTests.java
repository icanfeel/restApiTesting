import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class UserApiTests extends BaseApiTest {

    // Позитивный тест на регистрацию пользователя с валидными данными
    @Test
    public void testRegisterUserWithValidData() {
        given()
                .contentType(ContentType.URLENC)
                .formParam("username", "newUser")
                .formParam("email", "newuser@example.com")
                .formParam("password", "password123")
                .when()
                .post("/user/create")
                .then()
                .statusCode(201)
                .body("message", equalTo("User created successfully"));
    }

    // Негативный тест: регистрация с уже существующим email
    @Test
    public void testRegisterUserWithExistingEmail() {
        given()
                .contentType(ContentType.URLENC)
                .formParam("username", "existingUser")
                .formParam("email", "existinguser@example.com")
                .formParam("password", "password123")
                .when()
                .post("/user/create")
                .then()
                .statusCode(409)
                .body("message", containsString("Email already in use"));
    }

    // Негативный тест: регистрация с пустыми полями
    @Test
    public void testRegisterUserWithEmptyFields() {
        given()
                .contentType(ContentType.URLENC)
                .formParam("username", "")
                .formParam("email", "")
                .formParam("password", "")
                .when()
                .post("/user/create")
                .then()
                .statusCode(400)
                .body("errors.username", equalTo("Username cannot be empty"))
                .body("errors.email", equalTo("Email cannot be empty"))
                .body("errors.password", equalTo("Password cannot be empty"));
    }

    // Негативный тест: регистрация с некорректным email
    @Test
    public void testRegisterUserWithInvalidEmail() {
        given()
                .contentType(ContentType.URLENC)
                .formParam("username", "newUser")
                .formParam("email", "invalid-email")
                .formParam("password", "password123")
                .when()
                .post("/user/create")
                .then()
                .statusCode(400)
                .body("message", equalTo("Invalid email format"));
    }

    // Позитивный тест: получение списка пользователей, когда в системе зарегистрированы пользователи
    @Test
    public void testGetAllUsers() {
        given()
                .when()
                .get("/user/get")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0))  // Проверяем, что список не пустой
                .body("[0].username", notNullValue())  // Первый пользователь имеет имя пользователя
                .body("[0].email", containsString("@"));  // Email первого пользователя валидный
    }

    // Позитивный тест: получение списка пользователей, когда в системе нет зарегистрированных пользователей
    @Test
    public void testGetAllUsersWhenNoUsers() {
        given()
                .when()
                .get("/user/get")
                .then()
                .statusCode(200)
                .body("size()", equalTo(0));  // Проверка, что список пуст
    }
}