import io.qameta.allure.Step;
import io.restassured.RestAssured;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public class BaseApiTest {
    protected static String username;
    protected static String email;
    protected static String password;
    protected static SoftAssertions softAssertions;
    protected static ApiSteps apiSteps;

    @BeforeAll
    @Step("Настройка базового URI и таймаутов для API")
    public static void setup() {
        // Настройка RestAssured для использования базового URL
        RestAssured.baseURI = ConfigReader.getProperty("base.url");

        // Получение тестовых данных из файла property
        username = ConfigReader.getProperty("user.username");
        email = ConfigReader.getProperty("user.email");
        password = ConfigReader.getProperty("user.password");

        // Установка таймаутов для запросов
        int connectionTimeout = Integer.parseInt(ConfigReader.getProperty("timeout.connection"));
        int readTimeout = Integer.parseInt(ConfigReader.getProperty("timeout.read"));

        // Конфигурация таймаутов для RestAssured
        RestAssured.config = RestAssured.config()
                .httpClient(RestAssured.config().getHttpClientConfig()
                        .setParam("http.connection.timeout", connectionTimeout)
                        .setParam("http.socket.timeout", readTimeout)
                );
    }

    @BeforeEach
    public void initSoftAssertions() {
        // Инициализация SoftAssertions перед каждым тестом
        softAssertions = new SoftAssertions();
    }

    @BeforeEach
    public void setupSteps() {
        // Инициализация ApiSteps перед каждым тестом
        apiSteps = new ApiSteps();
    }
}
