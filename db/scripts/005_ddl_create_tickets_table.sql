CREATE TABLE IF NOT EXISTS tickets
(
    id SERIAL PRIMARY KEY,
    session_id INT NOT NULL REFERENCES sessions(id),
    pos_row INT NOT NULL,
    cell INT NOT NULL,
    user_id INT NOT NULL REFERENCES users(id),
    CONSTRAINT ticket_unique UNIQUE (session_id, pos_row, cell)
);

comment on table tickets is 'Билеты';
comment on column tickets.id is 'Идентификатор билета';
comment on column tickets.session_id is 'Идентификатор сеанса, на котрый приобретен билет';
comment on column tickets.pos_row is 'Ряд, на который приобретен билет';
comment on column tickets.cell is 'Место, на которое приобретен билет';
comment on column tickets.user_id is 'Идентификатор пользователя, который приобрел билет';
