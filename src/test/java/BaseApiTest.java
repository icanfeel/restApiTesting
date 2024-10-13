import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

public class BaseApiTest {
    @BeforeAll
    public static void setup() {
        // Устанавливаем базовый URI для всех тестов, наследующих этот класс
        RestAssured.baseURI = "http://3.73.86.8:3333";
    }
}
