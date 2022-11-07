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
 - продолжительность.
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

<b>Схема базы данных (БД) приложения:</b>
![Схема БД приложения filmorate](https://github.com/grigory-pc/java-filmorate/blob/main/filmorateDBscheme_07.png?raw=true)

<b>Примеры запросов к БД.</b>  
Получение списка всех фильмов:  
SELECT * FROM films;  

Получение списка топ 10 фильмов:  
SELECT f.*  
FROM film_like fl  
LEFT JOIN films f ON fl.film_id = f.film_id  
GROUP BY fl.film_id  
ORDER BY COUNT(fl.user_id) DESC  
LIMIT 10;    

Получение списка всех пользователей:  
SELECT * FROM users;

Получение списка друзей пользователя:  
SELECT u.*  
FROM friendship fs  
LEFT JOIN users u ON fs.user2_id;

---

Приложение написано на Java.
При разработке приложения использовались JAVA 11 и JUnit 5.

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=java&logoColor=white)




