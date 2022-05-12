package ru.yandex.practicum.filmorate.controller;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.yandex.practicum.filmorate.model.Film;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FilmControllerTest {
    private final URI uri = URI.create("http://localhost:8080/");
    private HttpClient httpClient;
    private HttpResponse<String> responseBody;

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    @BeforeEach
    public void beforeEach() {
        httpClient = HttpClient.newHttpClient();
    }

    @DisplayName("GIVEN a film with incorrect data " +
            "WHEN film POSTed " +
            "THEN server response that error occurred")
    @MethodSource("test1MethodSource")
    @ParameterizedTest(name = "{index} test for exception:  {0}. Should response code: {1}")
    void Test1_shouldGetExceptionForPostIncorrectDataOfFilm(String exception, int errorCode, Film film) {
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
        assertTrue(responseBody.statusCode() == errorCode);
    }

    @DisplayName("GIVEN a film with incorrect data " +
            "WHEN film PUTed " +
            "THEN server response that error occurred")
    @MethodSource("test2MethodSource")
    @ParameterizedTest(name = "{index} test for exception:  {0}. Should response code: {1}")
    void Test2_shouldGetExceptionForPutIncorrectDataOfFilm(String exception, int errorCode, Film film) {
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
        assertTrue(responseBody.statusCode() == errorCode);
    }

    private Stream<Arguments> test1MethodSource() {
        return Stream.of(
                Arguments.of("No Exception", 200, new Film(0, "cinema", "cinema description",
                        LocalDate.of(1967, 3, 25), Duration.ofMinutes(100))),
                Arguments.of("InvalidNameException", 500, new Film(0, "", "cinema description",
                        LocalDate.of(1967, 3, 25), Duration.ofMinutes(100))),
                Arguments.of("FilmAlreadyExistException", 500, new Film(0, "cinema",
                        "cinema description", LocalDate.of(1967, 3, 25),
                        Duration.ofMinutes(100))),
                Arguments.of("HighLengthDescriptionException", 500, new Film(0, "cinema1",
                        "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345" +
                                "678901234567890123456789012345678901234567890123456789012345678901234567890123456789012" +
                                "34567890123456789012345678901", LocalDate.of(1967, 0, 25),
                        Duration.ofMinutes(100))),
                Arguments.of("OldDateFilmException", 500, new Film(0, "cinema2",
                        "cinema description", LocalDate.of(1867, 3, 25), Duration.ofMinutes(100))),
                Arguments.of("NegativeDurationException", 500,
                        new Film(0, "cinema3", "cinema description", LocalDate.of(1967,
                                3, 25), Duration.ofMinutes(-1)))
        );
    }

    private Stream<Arguments> test2MethodSource() {
        return Stream.of(
                Arguments.of("InvalidNameException", 500, new Film(0, "", "cinema description",
                        LocalDate.of(1967, 3, 25), Duration.ofMinutes(100))),
                Arguments.of("HighLengthDescriptionException", 500, new Film(0, "cinema1",
                        "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345" +
                                "678901234567890123456789012345678901234567890123456789012345678901234567890123456789012" +
                                "34567890123456789012345678901", LocalDate.of(1967, 3, 25),
                        Duration.ofMinutes(100))),
                Arguments.of("OldDateFilmException", 500, new Film(0, "cinema2",
                        "cinema description", LocalDate.of(1867, 3, 25), Duration.ofMinutes(100))),
                Arguments.of("NegativeDurationException", 500,
                        new Film(0, "cinema3", "cinema description", LocalDate.of(1967,
                                3, 25), Duration.ofMinutes(-1)))
        );
    }
}

class LocalDateAdapter extends TypeAdapter<LocalDate> {
    private static final DateTimeFormatter formatterWriter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter formatterReader = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public void write(JsonWriter jsonWriter, LocalDate localDate) throws IOException {
        jsonWriter.value(localDate.format(formatterWriter));
    }

    @Override
    public LocalDate read(JsonReader jsonReader) throws IOException {
        return LocalDate.parse(jsonReader.nextString(), formatterReader);
    }
}

class DurationAdapter extends TypeAdapter<Duration> {

    @Override
    public void write(JsonWriter jsonWriter, Duration duration) throws IOException {
        jsonWriter.value(String.valueOf(duration));
    }

    @Override
    public Duration read(JsonReader jsonReader) throws IOException {
        return Duration.ofMinutes(jsonReader.nextLong());
    }
}