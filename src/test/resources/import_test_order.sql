insert into point (user_id, point_amount) values (1, 1000000),
                                                (2, 1000000),
                                                (3, 1000000),
                                                (4, 1000000),
                                                (5, 1000000);


insert into item (name, price, stock) values ('test', 5000, 5);

insert into orders (order_id, user_id, total_price, final_payment_price, order_status) values
                                                                                 (1,1, 5000, 5000, 'ORDER_CREATED'),
                                                                                 (2,2, 5000, 5000, 'ORDER_CREATED'),
                                                                                 (3,3, 5000, 5000, 'ORDER_CREATED'),
                                                                                 (4,4, 5000, 5000, 'ORDER_CREATED'),
                                                                                 (5,5, 5000, 5000, 'ORDER_CREATED');


insert into order_item (order_id, item_id, quantity, price) values
                                                                (1,1, 1, 5000),
                                                                (2,1, 1, 5000),
                                                                (3,1, 1, 5000),
                                                                (4,1, 1, 5000),
                                                                (5,1, 1, 5000);