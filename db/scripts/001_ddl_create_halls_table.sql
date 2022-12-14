CREATE TABLE IF NOT EXISTS halls
(
  id SERIAL PRIMARY KEY,
  name VARCHAR NOT NULL UNIQUE,
  rows int NOT NULL,
  cells int NOT NULL
);

comment on table halls is 'Кинозалы';
comment on column halls.id is 'Идентификатор кинозала';
comment on column halls.name is 'Название кинозала';
comment on column halls.rows is 'Число рядов в зале';
comment on column halls.cells is 'Число мест в каждом ряду';
