//package ru.yandex.practicum.filmorate.storage;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import ru.yandex.practicum.filmorate.exception.NotFoundException;
//import ru.yandex.practicum.filmorate.model.Film;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Реализация интерфейса для хранения фильмов
// */
//@Slf4j
//@Component
//public class InMemoryFilmStorage implements FilmStorage {
//    private final List<Film> films = new ArrayList<>();
//
//    /**
//     * Добавление фильма в список
//     */
//    public Film add(Film film) {
//        films.add(film);
//
//        return film;
//    }
//
//    /**
//     * Обновление фильма в списке
//     */
//    public Film update(Film filmExisting, Film filmForUpdate) {
//        filmExisting.setDescription(filmForUpdate.getDescription());
//        filmExisting.setName(filmForUpdate.getName());
//        filmExisting.setDuration(filmForUpdate.getDuration());
//        filmExisting.setReleaseDate(filmForUpdate.getReleaseDate());
//
//        return filmExisting;
//    }
//
//    /**
//     * Удаление фильма из списка
//     */
//    public Film delete(Film film) {
//        films.remove(film);
//
//        return film;
//    }
//
//    /**
//     * Получение списка фильмов
//     */
//    public List<Film> getFilmAll() {
//        return films;
//    }
//
//    /**
//     * Получение фильма из списка по ID
//     */
//    public Film getFilmById(long filmId) {
//        return films.stream()
//                .filter(p -> p.getId() == (filmId))
//                .findFirst()
//                .orElseThrow(() -> new NotFoundException(String.format("Фильм с id %d не найден",
//                        filmId)));
//    }
//}
