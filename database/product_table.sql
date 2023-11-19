create table product (
    product_id serial primary key,
    title varchar(100) not null,
    description varchar(255) null,
    release_date timestamptz null
);