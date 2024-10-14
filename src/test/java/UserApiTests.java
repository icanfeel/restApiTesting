import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserApiTests extends BaseApiTest {
    private ApiSteps userApiSteps;

    // 1. Регистрация нового пользователя с валидными данными
    @Test
    public void testRegisterUserWithValidData() {
        userApiSteps.registerUser(username, email, password, softAssertions);
        softAssertions.assertAll();
    }

    // 2. Попытка регистрации пользователя с уже существующим email
    @Test
    public void testRegisterUserWithExistingEmail() {
        userApiSteps.registerUser(username, email, password, softAssertions); // Первый раз
        userApiSteps.registerUserWithExistingEmail(username, email, password, softAssertions); // Второй раз с конфликтом
        softAssertions.assertAll();
    }

    // 3. Регистрация пользователя с пустыми полями
    @Test
    public void testRegisterUserWithEmptyFields() {
        userApiSteps.registerUserWithEmptyFields(softAssertions);
        softAssertions.assertAll();
    }

    // 4. Регистрация пользователя с некорректным email
    @Test
    public void testRegisterUserWithInvalidEmail() {
        String invalidEmail = "invalid-email";
        userApiSteps.registerUserWithInvalidEmail(username, invalidEmail, password, softAssertions);
        softAssertions.assertAll();
    }

    // 5. Получение списка пользователей, когда в системе зарегистрированы пользователи
    @Test
    public void testGetAllUsersWithRegisteredUsers() {
        userApiSteps.registerUser(username, email, password, softAssertions);
        userApiSteps.getAllUsers(softAssertions);
        softAssertions.assertAll();
    }

    // 6. Получение списка пользователей, когда в системе нет зарегистрированных пользователей
    @Test
    public void testGetNoUsers() {
        userApiSteps.getNoUsers(softAssertions);
        softAssertions.assertAll();
    }
}