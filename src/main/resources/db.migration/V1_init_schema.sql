create sequence if not exists user_seq start with 1 increment by 50;
create sequence if not exists note_seq start with 1 increment by 50;
create sequence if not exists product_seq start with 1 increment by 50;

create table if not exists users (
    id          bigint primary key,
    username    varchar(30) not null unique
);

create table if not exists notes (
    id          bigint primary key,
    text        varchar(1000) not null,
    created_at  timestamp(6) with time zone not null,
    updated_at  timestamp(6) with time zone not null,
    author_id   bigint not null
);

alter table notes
    add constraint fk_notes_author
    foreign key (author_id) references users(id);

create table if not exists products (
    id              bigint primary key,
    account_number  varchar(32) not null unique,
    balance         numeric(19,2) not null default 0,
    type            varchar(20) not null,
    created_at      timestamp(6) with time zone not null,
    updated_at      timestamp(6) with time zone not null,
    user_id         bigint not null
);

alter table products
    add constraint fk_products_user
    foreign key (user_id) references users(id);

create index if not exists ix_products_user on products(user_id);