package ru.yandex.practicum.filmorate.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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
    @MethodSource("test1MethodSource")
    @ParameterizedTest(name = "{index} test for exception:  {0}. Should response code: {1}")
    void Test1_shouldGetExceptionForPostIncorrectDataOfUser(String exception, int errorCode, User user) {
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
        assertTrue(responseBody.statusCode() == errorCode);
    }

    @DisplayName("GIVEN a user with incorrect data " +
            "WHEN film PUTed " +
            "THEN server response that error occurred")
    @MethodSource("test2MethodSource")
    @ParameterizedTest(name = "{index} test for exception:  {0}. Should response code: {1}")
    void Test2_shouldGetExceptionForPutIncorrectDataOfUser(String exception, int errorCode, User user) {
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
        assertTrue(responseBody.statusCode() == errorCode);
    }

    private Stream<Arguments> test1MethodSource() {
        return Stream.of(
                Arguments.of("No Exception", 200, new User(0, "test@test.ru", "user1", "user",
                        LocalDate.of(1990, 1, 1))),
                Arguments.of("UserAlreadyExistException", 500, new User(0, "test@test.ru", "user1",
                        "user", LocalDate.of(1990, 1, 1))),
                Arguments.of("InvalidEmailException, check blank", 500, new User(0, "",
                        "user1", "user", LocalDate.of(1990, 1, 1))),
                Arguments.of("InvalidEmailException, check contains @", 500, new User(0, "testtest.ru",
                        "user1", "user", LocalDate.of(1990, 1, 1))),
                Arguments.of("InvalidLoginException, check blank", 500, new User(0, "test@test.ru",
                        "", "user", LocalDate.of(1990, 1, 1))),
                Arguments.of("InvalidLoginException, check space", 500, new User(0, "test@test.ru",
                        "user 1", "user", LocalDate.of(1990, 1, 1))),
                Arguments.of("No exception when name is empty", 200, new User(0, "test1@test.ru",
                        "user1", "", LocalDate.of(1990, 1, 1))),
                Arguments.of("InFutureBirthdayException", 500, new User(0, "test@test.ru",
                        "user1", "user", LocalDate.MAX))
        );
    }

    private Stream<Arguments> test2MethodSource() {
        return Stream.of(
                Arguments.of("InvalidEmailException, check blank", 500, new User(0, "",
                        "user1", "user", LocalDate.of(1990, 1, 1))),
                Arguments.of("InvalidEmailException, check contains @", 500, new User(0, "testtest.ru",
                        "user1", "user", LocalDate.of(1990, 1, 1))),
                Arguments.of("InvalidLoginException, check blank", 500, new User(0, "test@test.ru",
                        "", "user", LocalDate.of(1990, 1, 1))),
                Arguments.of("InvalidLoginException, check space", 500, new User(0, "test@test.ru",
                        "user 1", "user", LocalDate.of(1990, 1, 1))),
                Arguments.of("No exception when name is empty", 200, new User(0, "test@test.ru",
                        "user1", "", LocalDate.of(1990, 1, 1))),
                Arguments.of("InFutureBirthdayException", 500, new User(0, "test@test.ru",
                        "user1", "user", LocalDate.MAX))
        );
    }
}

