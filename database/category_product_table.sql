create table category_product (
    category_id integer references category(category_id),
    product_id integer references product(product_id),
    primary key(category_id, product_id)
);