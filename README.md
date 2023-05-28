# java-filmorate
Template repository for Filmorate project.

![Блок схема](DB_Filmorate.png)


запрос всех фильмов
SELECT * FROM film;

запрос фильма по id
SELECT * FROM film WHERE film_id = 1;

запрос популярных фильмов
SELECT * FROM film ORDER BY rate DESC LIMIT 10;

запрос всех пользователей
SELECT * FROM user;

запрос пользователя по id
SELECT * FROM user WHERE user_id = 1;

запрос списка друзей
SELECT u.user_id, u.name FROM friends AS f JOIN user AS u ON f.friend_id = u.user_id;

запрос списка общих друзей
SELECT u.user_id, u.name FROM friends AS f 
    WHERE f.frisnd_id = ( SELECT u.user_id, u.name JOIN user AS u ON f.friend_id = u.user_id);