create sequence if not exists payment_seq start with 1 increment by 50;

create table if not exists payments(
    id          biging primary key,
    product_id  bigint not null,
    user_id     bigint not null,
    amount      numeric(19,2) not null,
    status      varchar(20) not null,
    created_at      timestamp(6) with time zone not null,
    updated_at      timestamp(6) with time zone not null
);