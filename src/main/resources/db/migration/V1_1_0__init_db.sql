create table if not exists public."user"
(
    id            bigint generated always as identity primary key,
    phone         varchar(12)  not null default 'UNKNOWN',
    email         varchar(100) not null default 'UNKNOWN',
    date_of_birth timestamp    not null,
    fullname      varchar(100) not null,
    login         varchar(100) not null unique,
    password      varchar(100) not null
);

create index if not exists phone_index ON "user" (phone);
create index if not exists email_index ON "user" (email);
create index if not exists login_index ON "user" (login);

comment on table "user" is 'Пользователь';
comment on column "user".phone is 'Телефон';
comment on column "user".email is 'Почта';
comment on column "user".date_of_birth is 'Дата рождения';
comment on column "user".fullname is 'ФИО';
comment on column "user".login is 'Логин';
comment on column "user".password is 'Пароль';


create table if not exists user_account
(
    id              bigint generated always as identity primary key,
    init_balance    decimal not null check ( init_balance >= 0 ),
    current_balance decimal not null check ( current_balance >= 0 ),
    user_id         bigint  not null unique
);

comment on table user_account is 'Аккаунт пользователя';
comment on column user_account.init_balance is 'Баланс счета на момент создания';
comment on column user_account.current_balance is 'Актуальный баланс счета';
comment on column user_account.user_id is 'Идентификатор юзера, к которому прикреплен счет';


create table if not exists token
(
    id         bigint generated always as identity primary key,
    token      varchar(200) not null unique,
    token_type varchar(10)  not null,
    revoked    bool         not null,
    expired    bool         not null,
    user_id    bigint       not null
);

create index if not exists token_index ON token (token);

comment on table token is 'Таблица для сбора токенов';
comment on column token.token is 'Сам токен';
comment on column token.token_type is 'Тип токена';
comment on column token.revoked is 'Отозван токен или нет';
comment on column token.expired is 'Валидный токен или нет';
comment on column token.user_id is 'Идентификатор юзера, к которому прикреплен токен';