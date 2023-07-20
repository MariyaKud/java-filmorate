
--Пользователь 1
  insert into USERS (EMAIL, USER_LOGIN, USER_NAME, BIRTHDAY)
  VALUES ('mail@mail.ru', 'Test', 'Nick Test','1946-12-14 15:20:00');

--Пользователь 2
  insert into USERS (EMAIL, USER_LOGIN, USER_NAME, BIRTHDAY)
  VALUES ('popular@mail.ru', 'Popular', 'Nick Popular','2000-01-01 00:00:00');

--Пользователь 3
  insert into USERS (EMAIL, USER_LOGIN, USER_NAME, BIRTHDAY)
  VALUES ('test@mail.ru', 'Test', 'Test Test','2000-01-01 00:00:00');

--Добавим в друзья пользователя 2 для пользователя 1
  insert into FRIENDS (USER_ID, FRIEND_ID) VALUES (1,2);

--Фильм 1
  insert into FILMS (FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID)
  VALUES ('nisi eiusmod', 'adipisicing','2022-12-14 15:20:00', 100, 1);

--Фильм 2
  insert into FILMS (FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID)
  VALUES ('New film', 'New film about friends','1999-01-15 00:00:00', 120, 3);

--Фильм 3
  insert into FILMS (FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID)
  VALUES ('Best film', 'Top of all','2023-01-01 00:00:00', 120, 4);

--Лайки фильмам (лайки 3 фильму от всех, лайк фильму 2 от пользователя 1)
  insert into LIKES (USER_ID, FILM_ID) VALUES (1,3);
  insert into LIKES (USER_ID, FILM_ID) VALUES (2,3);
  insert into LIKES (USER_ID, FILM_ID) VALUES (3,3);
  insert into LIKES (USER_ID, FILM_ID) VALUES (1,2);

--Жанры для фильма (лайк от пользователя 1 и 2 для фильма 2)
 insert into GENRE_FILMS (GENRE_ID, FILM_ID) VALUES (1,2);
 insert into GENRE_FILMS (GENRE_ID, FILM_ID) VALUES (2,2);