package ru.yandex.practicum.filmorate.controller;

import com.google.gson.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class FilmControllerTest {
    private final URI uri = URI.create("http://localhost:8080/");
    private HttpClient httpClient;
    private HttpResponse<String> responseBody;

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();

    @BeforeEach
    public void beforeEach() {
        httpClient = HttpClient.newHttpClient();
    }

    @DisplayName("GIVEN a film with incorrect data " +
            "WHEN film POSTed " +
            "THEN server response that error occurred")
    @MethodSource("test1And2MethodSource")
    @ParameterizedTest(name = "{index} test for exception:  {0}. Should response code: {1}")
    void Test1_shouldGetExceptionForPostIncorrectDataOfFilm(String exception,  int errorCode1, int errorCode2, Film film) {
        String jsonFilm = gson.toJson(film);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri + "films/"))
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

    @DisplayName("GIVEN a film with incorrect data " +
            "WHEN film PUTed " +
            "THEN server response that error occurred")
    @MethodSource("test1And2MethodSource")
    @ParameterizedTest(name = "{index} test for exception:  {0}. Should response code: {1}")
    void Test2_shouldGetExceptionForPutIncorrectDataOfFilm(String exception,  int errorCode1, int errorCode2, Film film) {
        String jsonFilm = gson.toJson(film);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri + "films/"))
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
                Arguments.of("No Exception", 200, 200, Film.builder()
                        .name("cinema")
                        .description("cinema description")
                        .releaseDate(LocalDate.of(1967, 3, 25))
                        .duration(100)
                        .build()),
                Arguments.of("InvalidNameException", 400, 500, Film.builder()
                        .id(0)
                        .name("")
                        .description("cinema description")
                        .releaseDate(LocalDate.of(1967, 3, 25))
                        .duration(100)
                        .build()),
                Arguments.of("HighLengthDescriptionException", 400, 500, Film.builder()
                        .id(0)
                        .name("cinema")
                        .description("1234567890123456789012345678901234567890123456789012345678901234567890123456789012" +
                                "345678901234567890123456789012345678901234567890123456789012345678901234567890123456789" +
                                "01234567890123456789012345678901")
                        .releaseDate(LocalDate.of(1967, 3, 25))
                        .duration(100)
                        .build()),
                Arguments.of("OldDateFilmException", 400, 500, Film.builder()
                        .id(0)
                        .name("cinema")
                        .description("cinema description")
                        .releaseDate(LocalDate.of(1867, 3, 25))
                        .duration(100)
                        .build()),
                Arguments.of("NegativeDurationException", 400, 500, Film.builder()
                        .id(0)
                        .name("cinema")
                        .description("cinema description")
                        .releaseDate(LocalDate.of(1967, 3, 25))
                        .duration(-1)
                        .build()));
    }
}


