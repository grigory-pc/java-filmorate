# java-filmorate (агрегатор информации о фильмах)
---
Данный проект представляет из себя агрегатор фильмографии, который позволяет получать информацию о фильмах и
пользователях ресурса.  
Приложение позволяет:
1. Добавить новый фильм на ресурс.
2. Удалить фильм.
3. Поставить оценку(лайк) фильму.
4. Обновить информацию о фильме.
5. Получить список всех фильмов.
6. Получить список фильмов по определенным критериям (фильтрам).
7. Информация о фильмах включает:
 - название;
 - жанр;
 - рейтинг;
 - описание;
 - дату выпуска;
 - продолжительность;
 - рейтинг.
4. Зарегистрировать пользователя на ресурсе.
5. Удалить пользователя
6. Обновить информацию пользователя ресурса.
7. Добавить другого пользователя в друзья.
8. Удалить пользователя из списка друзей.
9. Получить список всех пользователей ресурса.
10. Получить список пльзователей по определенным критериям (фильтрам).
11. Информация о пользователях включает:
 - email;
 - логин;
 - имя;
 - фамилию;
 - дату рождения.
8. Получить список друзей пользователя.
9. Посмотреть список общих друзей пользователей.
10. Посмотреть статус дружбы между двумя пользователями.

---

Схема базы данных (БД) приложения:
![Схема БД приложения filmorate](https://github.com/grigory-pc/java-filmorate/blob/db-scheme/filmorateDBscheme_04.png?raw=true)

Примеры запросов к БД.
Получение списка всех фильмов:  
SELECT *  

FROM film  

Получение списка фильмов с категорией "G" и жанром "Comedy":
SELECT f.name AS film_name,  
    f.release_date  
FROM film AS f  
INNER JOIN genre AS g ON f.genre_id = g.genre_id  
INNER JOIN rating AS r ON f.rating_id = r.rating_id  
WHERE g.name LIKE "Comedy"  
    AND r.name LIKE "G"  
GROUP BY film_name  
ORDER BY film_name;  


Получение списка топ 10 фильмов:  
SELECT f.name AS film_name,  
    f.release_date,  
    COUNT(fl.user_email)  
FROM film AS f  
INNER JOIN film_like AS fl ON fl.film_id = f.id  
GROUP BY fl.film_id  
ORDER BY film_name;  

Получение списка всех пользователей:  
SELECT *  

FROM user

Получение списка друзей пользователя:  
SELECT fu.user2 AS friends   
FROM friendship_user AS fu  
INNER JOIN user AS u ON u.email = fu.user1_email  
WHEN u.email LIKE "john@mail.com"  
ORDER BY friends;  


---

Приложение написано на Java.
При разработке приложения использовались JAVA 11 и JUnit 5.

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=java&logoColor=white)




