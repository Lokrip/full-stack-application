create schema if not exists catalogue;

-- для схемы мы создаем таблицу t_product
create table catalogue.t_product(
    id serial primary key,
    c_title varchar(50) not null check (length(trim(c_title)) >= 3),
    c_details varchar(1000)
);
