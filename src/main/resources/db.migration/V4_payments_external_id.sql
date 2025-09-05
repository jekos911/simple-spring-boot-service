alter table payments add column if not exists external_id varchar(64);

create unique index if not exists ux_pay_external
    on payments(external_id)
    where external_id is not null;