DROP TABLE users, friends, films, likes, mpa, genre, film_genre CASCADE;

CREATE TABLE IF NOT EXISTS users (
        user_id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
        name varchar(40),
        login varchar(40) NOT NULL,
        email varchar(40) NOT NULL,
        birthday timestamp
);

CREATE TABLE IF NOT EXISTS friends (
        user_id bigint,
        friend_id bigint
);

CREATE TABLE IF NOT EXISTS films (
        film_id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
        name varchar(40) NOT NULL,
        description varchar,
        duration int,
        release_date timestamp,
        mpa_id int
);

CREATE TABLE IF NOT EXISTS likes (
       user_id bigint,
       film_id bigint
);

CREATE TABLE IF NOT EXISTS mpa (
       mpa_id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
       mpa_name varchar(40) NOT NULL
);

CREATE TABLE IF NOT EXISTS genre (
       genre_id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
       name varchar(40) NOT NULL
);

CREATE TABLE IF NOT EXISTS film_genre (
       genre_id int,
       film_id bigint
);