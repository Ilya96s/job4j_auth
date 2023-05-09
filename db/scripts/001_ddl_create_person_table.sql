create table person
(
    id       serial primary key not null,
    login    varchar(2000) unique,
    password varchar(2000)
);

comment on table person is 'Таблица со пользователями';
comment on column person.id is 'Идентификатор';
comment on column person.login is 'Логин';
comment on column person.password is 'Пароль';