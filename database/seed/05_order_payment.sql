INSERT INTO orders (id, table_id, account_id, status, created_at, total_amount) VALUES
                                                                                    (1, 1, 1, 'OPEN', NOW(), 80000),
                                                                                    (2, 2, 1, 'OPEN', NOW(), 50000);
INSERT INTO order_detail (order_id, product_id, quantity, price_at_order) VALUES
                                                                              (1, 1, 2, 25000),
                                                                              (1, 2, 1, 30000),
                                                                              (2, 1, 1, 25000);

INSERT INTO payments (order_id, amount, method, status, paid_at) VALUES
    (1, 80000, 'CASH', 'SUCCESS', NOW());