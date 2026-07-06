-- 1. Khởi tạo Cơ sở dữ liệu (coffee_house)
CREATE DATABASE IF NOT EXISTS coffee_house;
USE coffee_house;

-- Drop các bảng cũ theo thứ tự ngược của khóa ngoại để tránh lỗi ràng buộc
DROP TABLE IF EXISTS payments;
DROP TABLE IF EXISTS order_detail;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS recipes;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS dining_tables;
DROP TABLE IF EXISTS ingredients;
DROP TABLE IF EXISTS account;

-- 2. Tạo bảng danh mục (categories)
CREATE TABLE categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

-- 3. Tạo bảng sản phẩm (products)
CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price DOUBLE NOT NULL,
    image VARCHAR(255),
    category_id BIGINT NOT NULL,
    available BIT(1) NOT NULL DEFAULT 1,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE RESTRICT
);

-- 4. Tạo bảng bàn ăn (dining_tables)
CREATE TABLE dining_tables (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    status VARCHAR(255) NOT NULL DEFAULT 'AVAILABLE'
);

-- 5. Tạo bảng nguyên liệu (ingredients)
CREATE TABLE ingredients (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    unit VARCHAR(255) NOT NULL,
    stock_quantity DOUBLE NOT NULL,
    min_stock_quantity DOUBLE NOT NULL
);

-- 6. Tạo bảng công thức món (recipes)
CREATE TABLE recipes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    ingredient_id BIGINT NOT NULL,
    quantity DOUBLE NOT NULL,
    UNIQUE KEY unique_product_ingredient (product_id, ingredient_id),
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    FOREIGN KEY (ingredient_id) REFERENCES ingredients(id) ON DELETE CASCADE
);

-- 7. Tạo bảng tài khoản (account - tương ứng thực thể Account)
CREATE TABLE account (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(255),
    role VARCHAR(255)
);

-- 8. Tạo bảng đơn hàng (orders)
CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    table_id BIGINT NOT NULL,
    account_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at DATETIME,
    total_amount DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    FOREIGN KEY (table_id) REFERENCES dining_tables(id) ON DELETE RESTRICT,
    FOREIGN KEY (account_id) REFERENCES account(id) ON DELETE RESTRICT
);

-- 9. Tạo bảng chi tiết đơn hàng (order_detail)
CREATE TABLE order_detail (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    price_at_order DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE RESTRICT
);

-- 10. Tạo bảng thanh toán (payments)
CREATE TABLE payments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    method VARCHAR(50),
    status VARCHAR(20),
    paid_at DATETIME,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
);

-- ==========================================
-- 11. CHÈN DỮ LIỆU MẪU (SEED DATA)
-- ==========================================

-- Chèn danh mục
INSERT INTO categories (id, name) VALUES (1, 'Cà phê');
INSERT INTO categories (id, name) VALUES (2, 'Trà');
INSERT INTO categories (id, name) VALUES (3, 'Bánh ngọt');

-- Chèn sản phẩm
INSERT INTO products (id, name, price, image, category_id, available) VALUES (1, 'Cà phê đen', 25000.0, 'cf_den.jpg', 1, 1);
INSERT INTO products (id, name, price, image, category_id, available) VALUES (2, 'Cà phê sữa', 29000.0, 'cf_sua.jpg', 1, 1);
INSERT INTO products (id, name, price, image, category_id, available) VALUES (3, 'Trà đào', 35000.0, 'tra_dao.jpg', 2, 1);
INSERT INTO products (id, name, price, image, category_id, available) VALUES (4, 'Bánh Tiramisu', 40000.0, 'tiramisu.jpg', 3, 1);

-- Chèn bàn ăn
INSERT INTO dining_tables (name, status) VALUES ('Bàn 1', 'AVAILABLE');
INSERT INTO dining_tables (name, status) VALUES ('Bàn 2', 'AVAILABLE');
INSERT INTO dining_tables (name, status) VALUES ('Bàn 3', 'OCCUPIED');
INSERT INTO dining_tables (name, status) VALUES ('Bàn 4', 'AVAILABLE');
INSERT INTO dining_tables (name, status) VALUES ('Bàn 5', 'AVAILABLE');
INSERT INTO dining_tables (name, status) VALUES ('Bàn 6', 'AVAILABLE');
INSERT INTO dining_tables (name, status) VALUES ('Bàn 7', 'OCCUPIED');
INSERT INTO dining_tables (name, status) VALUES ('Bàn 8', 'AVAILABLE');

-- Chèn nguyên liệu
INSERT INTO ingredients (id, name, unit, stock_quantity, min_stock_quantity) VALUES (1, 'Hạt cà phê', 'g', 5000.0, 1000.0);
INSERT INTO ingredients (id, name, unit, stock_quantity, min_stock_quantity) VALUES (2, 'Sữa đặc', 'ml', 3000.0, 500.0);
INSERT INTO ingredients (id, name, unit, stock_quantity, min_stock_quantity) VALUES (3, 'Đường cát', 'g', 10000.0, 2000.0);
INSERT INTO ingredients (id, name, unit, stock_quantity, min_stock_quantity) VALUES (4, 'Trà túi lọc', 'túi', 100.0, 20.0);
INSERT INTO ingredients (id, name, unit, stock_quantity, min_stock_quantity) VALUES (5, 'Đào miếng', 'miếng', 5.0, 10.0);
INSERT INTO ingredients (id, name, unit, stock_quantity, min_stock_quantity) VALUES (6, 'Sữa tươi', 'ml', 400.0, 1000.0);

-- Chèn công thức món
INSERT INTO recipes (product_id, ingredient_id, quantity) VALUES (1, 1, 20.0);
INSERT INTO recipes (product_id, ingredient_id, quantity) VALUES (1, 3, 10.0);
INSERT INTO recipes (product_id, ingredient_id, quantity) VALUES (2, 1, 20.0);
INSERT INTO recipes (product_id, ingredient_id, quantity) VALUES (2, 2, 30.0);
INSERT INTO recipes (product_id, ingredient_id, quantity) VALUES (3, 4, 1.0);
INSERT INTO recipes (product_id, ingredient_id, quantity) VALUES (3, 5, 3.0);
INSERT INTO recipes (product_id, ingredient_id, quantity) VALUES (3, 3, 15.0);
