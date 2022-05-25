package ru.yandex.practicum.filmorate.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class UserControllerTest {
    private final URI uri = URI.create("http://localhost:8080/");
    private HttpClient httpClient;
    private HttpResponse<String> responseBody;

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new ru.yandex.practicum.filmorate.controller.LocalDateAdapter())
            .create();

    @BeforeEach
    public void beforeEach() {
        httpClient = HttpClient.newHttpClient();
    }

    @DisplayName("GIVEN a user with incorrect data " +
            "WHEN user POSTed " +
            "THEN server response that error occurred")
    @MethodSource("test1And2MethodSource")
    @ParameterizedTest(name = "{index} test for exception:  {0}. Should response code: {1}")
    void Test1_shouldGetExceptionForPostIncorrectDataOfUser(String exception,  int errorCode1, int errorCode2, User user) {
        String jsonFilm = gson.toJson(user);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri + "users/"))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonFilm))
                .build();

        try {
            responseBody = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (responseBody.statusCode() != 200) {
                System.out.println("Что-то пошло не так. Сервер вернул код состояния: " + responseBody.statusCode());
            }
        } catch (NullPointerException | IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку. Ошибка: " + e.getMessage());
        }
        assertTrue((responseBody.statusCode() == errorCode1) || (responseBody.statusCode() == errorCode2));
    }

    @DisplayName("GIVEN a user with incorrect data " +
            "WHEN film PUTed " +
            "THEN server response that error occurred")
    @MethodSource("test1And2MethodSource")
    @ParameterizedTest(name = "{index} test for exception:  {0}. Should response code: {1}")
    void Test2_shouldGetExceptionForPutIncorrectDataOfUser(String exception,  int errorCode1, int errorCode2, User user) {
        String jsonFilm = gson.toJson(user);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri + "users/"))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(jsonFilm))
                .build();

        try {
            responseBody = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (responseBody.statusCode() != 200) {
                System.out.println("Что-то пошло не так. Сервер вернул код состояния: " + responseBody.statusCode());
            }
        } catch (NullPointerException | IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку. Ошибка: " + e.getMessage());
        }
        assertTrue((responseBody.statusCode() == errorCode1) || (responseBody.statusCode() == errorCode2));
    }

    private Stream<Arguments> test1And2MethodSource() {
        return Stream.of(
                Arguments.of("No Exception", 200, 200, User.builder()
                        .email("test@test.ru")
                        .login("user1")
                        .name("user")
                        .birthday(LocalDate.of(1990, 1, 1))
                        .build()),
                Arguments.of("InvalidEmailException, check blank", 400, 500, User.builder()
                        .id(0)
                        .email("")
                        .login("user1")
                        .name("user")
                        .birthday(LocalDate.of(1990, 1, 1))
                        .build()),
                Arguments.of("InvalidEmailException, check contains @",  400, 500, User.builder()
                        .id(0)
                        .email("testtest.ru")
                        .login("user1")
                        .name("user")
                        .birthday(LocalDate.of(1990, 1, 1))
                        .build()),
                Arguments.of("InvalidLoginException, check blank",  400, 500, User.builder()
                        .id(0)
                        .email("test@test.ru")
                        .login("")
                        .name("user")
                        .birthday(LocalDate.of(1990, 1, 1))
                        .build()),
                Arguments.of("InvalidLoginException, check space",  400, 500, User.builder()
                        .id(0)
                        .email("test@test.ru")
                        .login("user 1")
                        .name("user")
                        .birthday(LocalDate.of(1990, 1, 1))
                        .build()),
                Arguments.of("No exception when name is empty", 200, 200, User.builder()
                        .id(0)
                        .email("test@test.ru")
                        .login("user1")
                        .name("")
                        .birthday(LocalDate.of(1990, 1, 1))
                        .build()),
                Arguments.of("InFutureBirthdayException",  400, 500, User.builder()
                        .id(0)
                        .email("test@test.ru")
                        .login("user1")
                        .name("user")
                        .birthday(LocalDate.MAX)
                        .build()));
    }
}

