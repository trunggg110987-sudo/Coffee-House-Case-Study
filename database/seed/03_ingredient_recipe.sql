INSERT INTO ingredients (id, name, unit, stock_quantity, min_stock_quantity) VALUES (1, 'Hạt cà phê', 'g', 5000.0, 1000.0);
INSERT INTO ingredients (id, name, unit, stock_quantity, min_stock_quantity) VALUES (2, 'Sữa đặc', 'ml', 3000.0, 500.0);
INSERT INTO ingredients (id, name, unit, stock_quantity, min_stock_quantity) VALUES (3, 'Đường cát', 'g', 10000.0, 2000.0);
INSERT INTO ingredients (id, name, unit, stock_quantity, min_stock_quantity) VALUES (4, 'Trà túi lọc', 'túi', 100.0, 20.0);
INSERT INTO ingredients (id, name, unit, stock_quantity, min_stock_quantity) VALUES (5, 'Đào miếng', 'miếng', 5.0, 10.0);
INSERT INTO ingredients (id, name, unit, stock_quantity, min_stock_quantity) VALUES (6, 'Sữa tươi', 'ml', 400.0, 1000.0);

INSERT INTO recipes (product_id, ingredient_id, quantity) VALUES (1, 1, 20.0);
INSERT INTO recipes (product_id, ingredient_id, quantity) VALUES (1, 3, 10.0);
INSERT INTO recipes (product_id, ingredient_id, quantity) VALUES (2, 1, 20.0);
INSERT INTO recipes (product_id, ingredient_id, quantity) VALUES (2, 2, 30.0);
INSERT INTO recipes (product_id, ingredient_id, quantity) VALUES (3, 4, 1.0);
INSERT INTO recipes (product_id, ingredient_id, quantity) VALUES (3, 5, 3.0);
INSERT INTO recipes (product_id, ingredient_id, quantity) VALUES (3, 3, 15.0);
