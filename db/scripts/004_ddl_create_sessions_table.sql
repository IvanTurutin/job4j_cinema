CREATE TABLE IF NOT EXISTS sessions
(
  id SERIAL PRIMARY KEY,
  name VARCHAR,
  hall_id INT NOT NULL REFERENCES halls(id)
);

comment on table sessions is 'Сеансы фильмов';
comment on column sessions.id is 'Идентификатор сеанса';
comment on column sessions.name is 'Название сеанса';
comment on column sessions.hall_id is 'Идентификатор зала, в котором проходит сеанс';
