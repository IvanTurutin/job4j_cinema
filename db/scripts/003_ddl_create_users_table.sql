CREATE TABLE IF NOT EXISTS  users
(
  id SERIAL PRIMARY KEY,
  username VARCHAR NOT NULL,
  password VARCHAR NOT NULL,
  email VARCHAR NOT NULL UNIQUE,
  phone VARCHAR NOT NULL UNIQUE
);

comment on table users is 'Пользователи сайта';
comment on column users.id is 'Идентификатор пользователя';
comment on column users.username is 'Имя пользователя';
comment on column users.password is 'Пароль пользователя';
comment on column users.email is 'Почта пользователя';
comment on column users.phone is 'Телефонныый номер пользователя';
