-- ============================================
-- СКРИПТ СОЗДАНИЯ И НАПОЛНЕНИЯ БАЗЫ ДАННЫХ
-- Food Delivery Service
-- ============================================

-- Удаляем существующие таблицы (в обратном порядке из-за foreign keys)
DROP TABLE IF EXISTS
    user_events,
    notification_preferences,
    courier_locations,
    reviews,
    payments,
    order_items,
    orders,
    menu_items,
    menu_categories,
    restaurants,
    users CASCADE;

-- Удаляем существующие типы ENUM
DROP TYPE IF EXISTS user_role CASCADE;
DROP TYPE IF EXISTS restaurant_status CASCADE;
DROP TYPE IF EXISTS order_status CASCADE;
DROP TYPE IF EXISTS payment_status CASCADE;
DROP TYPE IF EXISTS payment_method CASCADE;
DROP TYPE IF EXISTS notification_channel CASCADE;
DROP TYPE IF EXISTS notification_type CASCADE;
DROP TYPE IF EXISTS event_type CASCADE;

-- ============================================
-- 1. СОЗДАНИЕ ТИПОВ ENUM
-- ============================================

CREATE TYPE user_role AS ENUM ('CUSTOMER', 'COURIER', 'ADMIN', 'RESTAURANT_MANAGER');
CREATE TYPE restaurant_status AS ENUM ('ACTIVE', 'INACTIVE', 'CLOSED_TEMPORARILY');
CREATE TYPE order_status AS ENUM (
    'CREATED',
    'PAID',
    'KITCHEN_ACCEPTED',
    'COOKING',
    'READY_FOR_PICKUP',
    'ON_THE_WAY',
    'DELIVERED',
    'CANCELLED'
    );
CREATE TYPE payment_status AS ENUM ('PENDING', 'SUCCEEDED', 'FAILED', 'REFUNDED');
CREATE TYPE payment_method AS ENUM ('CARD', 'CASH', 'CARD_ONLINE');
CREATE TYPE notification_channel AS ENUM ('EMAIL', 'SMS', 'PUSH');
CREATE TYPE notification_type AS ENUM ('ORDER_STATUS_CHANGED', 'PROMOTION', 'SYSTEM');
CREATE TYPE event_type AS ENUM ('VIEW_RESTAURANT', 'SEARCH', 'ADD_TO_CART', 'PLACE_ORDER');

-- ============================================
-- 2. СОЗДАНИЕ ТАБЛИЦ
-- ============================================

-- 2.1 Таблица пользователей
CREATE TABLE users
(
    user_id       SERIAL PRIMARY KEY,
    email         VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255)        NOT NULL,
    full_name     VARCHAR(100)        NOT NULL,
    phone_number  VARCHAR(20),
    role          user_role           NOT NULL,
    avatar_url    TEXT,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_active     BOOLEAN   DEFAULT TRUE
);

-- 2.2 Таблица ресторанов
CREATE TABLE restaurants
(
    restaurant_id SERIAL PRIMARY KEY,
    manager_id    INTEGER      REFERENCES users (user_id) ON DELETE SET NULL,
    name          VARCHAR(100) NOT NULL,
    description   TEXT,
    logo_url      TEXT,
    address       TEXT         NOT NULL,
    -- coordinates POINT, -- раскомментировать при необходимости
    status        restaurant_status DEFAULT 'ACTIVE',
    rating        DECIMAL(3, 2)     DEFAULT 0.0,
    created_at    TIMESTAMP         DEFAULT CURRENT_TIMESTAMP
);

-- 2.3 Таблица категорий меню
CREATE TABLE menu_categories
(
    category_id   SERIAL PRIMARY KEY,
    restaurant_id INTEGER     NOT NULL REFERENCES restaurants (restaurant_id) ON DELETE CASCADE,
    name          VARCHAR(50) NOT NULL,
    sort_order    INTEGER DEFAULT 0,
    is_active     BOOLEAN DEFAULT TRUE
);

-- 2.4 Таблица блюд/позиций меню
CREATE TABLE menu_items
(
    item_id              SERIAL PRIMARY KEY,
    category_id          INTEGER        NOT NULL REFERENCES menu_categories (category_id) ON DELETE CASCADE,
    name                 VARCHAR(100)   NOT NULL,
    description          TEXT,
    price                DECIMAL(10, 2) NOT NULL CHECK (price >= 0),
    image_url            TEXT,
    is_available         BOOLEAN   DEFAULT TRUE,
    cooking_time_minutes INTEGER   DEFAULT 15,
    calories             INTEGER,
    created_at           TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2.5 Таблица заказов
CREATE TABLE orders
(
    order_id         SERIAL PRIMARY KEY,
    customer_id      INTEGER        NOT NULL REFERENCES users (user_id),
    restaurant_id    INTEGER        NOT NULL REFERENCES restaurants (restaurant_id),
    courier_id       INTEGER REFERENCES users (user_id),
    status           order_status DEFAULT 'CREATED',
    total_amount     DECIMAL(10, 2) NOT NULL CHECK (total_amount >= 0),
    delivery_address TEXT           NOT NULL,
    -- delivery_coordinates POINT,
    customer_notes   TEXT,
    created_at       TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    delivered_at     TIMESTAMP
);

-- 2.6 Таблица позиций в заказе
CREATE TABLE order_items
(
    order_item_id        SERIAL PRIMARY KEY,
    order_id             INTEGER        NOT NULL REFERENCES orders (order_id) ON DELETE CASCADE,
    menu_item_id         INTEGER        NOT NULL REFERENCES menu_items (item_id),
    quantity             INTEGER        NOT NULL CHECK (quantity > 0),
    price_at_order_time  DECIMAL(10, 2) NOT NULL CHECK (price_at_order_time >= 0),
    special_instructions TEXT
);

-- 2.7 Таблица платежей
CREATE TABLE payments
(
    payment_id          SERIAL PRIMARY KEY,
    order_id            INTEGER UNIQUE NOT NULL REFERENCES orders (order_id),
    amount              DECIMAL(10, 2) NOT NULL CHECK (amount >= 0),
    status              payment_status DEFAULT 'PENDING',
    payment_method      payment_method,
    external_payment_id TEXT,
    created_at          TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,
    processed_at        TIMESTAMP
);

-- 2.8 Таблица отзывов
CREATE TABLE reviews
(
    review_id         SERIAL PRIMARY KEY,
    order_id          INTEGER UNIQUE NOT NULL REFERENCES orders (order_id),
    customer_id       INTEGER        NOT NULL REFERENCES users (user_id),
    restaurant_rating INTEGER        NOT NULL CHECK (restaurant_rating >= 1 AND restaurant_rating <= 5),
    courier_rating    INTEGER CHECK (courier_rating >= 1 AND courier_rating <= 5),
    comment_text      TEXT,
    created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2.9 Таблица геолокации курьеров
CREATE TABLE courier_locations
(
    courier_id INTEGER                             NOT NULL REFERENCES users (user_id),
    -- coordinates POINT NOT NULL,
    latitude   DECIMAL(9, 6),
    longitude  DECIMAL(9, 6),
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    PRIMARY KEY (courier_id)
);

-- 2.10 Таблица настроек уведомлений (для будущего Notification Hub)
CREATE TABLE notification_preferences
(
    user_id           INTEGER              NOT NULL REFERENCES users (user_id),
    channel           notification_channel NOT NULL,
    notification_type notification_type    NOT NULL,
    is_enabled        BOOLEAN DEFAULT TRUE,
    PRIMARY KEY (user_id, channel, notification_type)
);

-- 2.11 Таблица событий пользователей (для будущего Analytics Service)
CREATE TABLE user_events
(
    event_id   SERIAL PRIMARY KEY,
    user_id    INTEGER REFERENCES users (user_id),
    event_type event_type NOT NULL,
    entity_id  TEXT,
    event_data JSONB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- 3. СОЗДАНИЕ ИНДЕКСОВ ДЛЯ ПРОИЗВОДИТЕЛЬНОСТИ
-- ============================================

CREATE INDEX idx_users_email ON users (email);
CREATE INDEX idx_users_role ON users (role);
CREATE INDEX idx_restaurants_manager ON restaurants (manager_id);
CREATE INDEX idx_restaurants_status ON restaurants (status);
CREATE INDEX idx_menu_categories_restaurant ON menu_categories (restaurant_id);
CREATE INDEX idx_menu_items_category ON menu_items (category_id);
CREATE INDEX idx_menu_items_available ON menu_items (is_available);
CREATE INDEX idx_orders_customer ON orders (customer_id);
CREATE INDEX idx_orders_restaurant ON orders (restaurant_id);
CREATE INDEX idx_orders_courier ON orders (courier_id);
CREATE INDEX idx_orders_status ON orders (status);
CREATE INDEX idx_orders_created_at ON orders (created_at);
CREATE INDEX idx_order_items_order ON order_items (order_id);
CREATE INDEX idx_payments_order ON payments (order_id);
CREATE INDEX idx_payments_status ON payments (status);
CREATE INDEX idx_reviews_order ON reviews (order_id);
CREATE INDEX idx_reviews_customer ON reviews (customer_id);
CREATE INDEX idx_courier_locations_updated ON courier_locations (updated_at);
CREATE INDEX idx_user_events_user ON user_events (user_id);
CREATE INDEX idx_user_events_created ON user_events (created_at);
CREATE INDEX idx_user_events_type ON user_events (event_type);

-- ============================================
-- 4. ТРИГГЕР ДЛЯ ОБНОВЛЕНИЯ UPDATED_AT
-- ============================================

CREATE OR REPLACE FUNCTION update_updated_at_column()
    RETURNS TRIGGER AS
$$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_orders_updated_at
    BEFORE UPDATE
    ON orders
    FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

-- ============================================
-- 5. НАПОЛНЕНИЕ ТЕСТОВЫМИ ДАННЫМИ
-- ============================================

-- 5.1 Пользователи
INSERT INTO users (email, password_hash, full_name, phone_number, role, is_active)
VALUES
-- Администратор
('admin@fooddelivery.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye7Z7c3K5z8lJc', 'Администратор Системы', '+79991112233',
 'ADMIN', TRUE),
-- Менеджеры ресторанов
('italia.manager@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye7Z7c3K5z8lJc', 'Марио Росси', '+79992223344',
 'RESTAURANT_MANAGER', TRUE),
('sushi.manager@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye7Z7c3K5z8lJc', 'Такеши Ямамото', '+79993334455',
 'RESTAURANT_MANAGER', TRUE),
-- Курьеры
('courier.ivanov@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye7Z7c3K5z8lJc', 'Иван Иванов', '+79994445566', 'COURIER',
 TRUE),
('courier.petrova@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye7Z7c3K5z8lJc', 'Мария Петрова', '+79995556677', 'COURIER',
 TRUE),
-- Клиенты
('customer.alex@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye7Z7c3K5z8lJc', 'Алексей Смирнов', '+79996667788',
 'CUSTOMER', TRUE),
('customer.olga@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye7Z7c3K5z8lJc', 'Ольга Козлова', '+79997778899', 'CUSTOMER',
 TRUE);

-- 5.2 Рестораны
INSERT INTO restaurants (manager_id, name, description, address, status, rating)
VALUES (2, 'La Bella Italia', 'Настоящая итальянская кухня от шеф-повара из Рима', 'ул. Пушкина, д. 10', 'ACTIVE', 4.7),
       (3, 'Tokyo Sushi', 'Свежие суши и роллы с доставкой', 'пр. Ленина, д. 25', 'ACTIVE', 4.9),
       (2, 'Pizza Presto', 'Лучшая пицца в городе', 'ул. Гагарина, д. 15', 'ACTIVE', 4.5);

-- 5.3 Категории меню
INSERT INTO menu_categories (restaurant_id, name, sort_order)
VALUES
-- La Bella Italia
(1, 'Пицца', 1),
(1, 'Паста', 2),
(1, 'Салаты', 3),
(1, 'Напитки', 4),
-- Tokyo Sushi
(2, 'Суши', 1),
(2, 'Роллы', 2),
(2, 'Сеты', 3),
(2, 'Напитки', 4),
-- Pizza Presto
(3, 'Пицца', 1),
(3, 'Закуски', 2),
(3, 'Десерты', 3);

-- 5.4 Блюда
INSERT INTO menu_items (category_id, name, description, price, is_available, cooking_time_minutes)
VALUES
-- La Bella Italia - Пицца
(1, 'Маргарита', 'Томатный соус, моцарелла, базилик', 450.00, TRUE, 20),
(1, 'Пепперони', 'Томатный соус, моцарелла, пепперони', 550.00, TRUE, 25),
-- La Bella Italia - Паста
(2, 'Карбонара', 'Спагетти, бекон, сливочный соус, пармезан', 480.00, TRUE, 15),
(2, 'Болоньезе', 'Спагетти, мясной соус болоньезе', 520.00, TRUE, 18),
-- Tokyo Sushi - Суши
(5, 'Филадельфия', 'Лосось, сливочный сыр, огурец', 320.00, TRUE, 10),
(5, 'Калифорния', 'Краб, авокадо, огурец, икра', 280.00, TRUE, 10),
-- Tokyo Sushi - Роллы
(6, 'Дракон', 'Угорь, огурец, авокадо, соус унаги', 420.00, TRUE, 12),
(6, 'Аляска', 'Лосось, сливочный сыр, огурец', 350.00, TRUE, 10),
-- Pizza Presto - Пицца
(9, '4 Сыра', 'Моцарелла, горгонзола, пармезан, фонтина', 590.00, TRUE, 22),
(9, 'Гавайская', 'Ветчина, ананас, моцарелла', 520.00, FALSE, 20);
-- Нет в наличии!

-- 5.5 Заказы
INSERT INTO orders (customer_id, restaurant_id, courier_id, status, total_amount, delivery_address, customer_notes)
VALUES (5, 1, 4, 'DELIVERED', 1000.00, 'ул. Советская, д. 5, кв. 12', 'Позвонить за 10 минут'),
       (6, 2, NULL, 'COOKING', 600.00, 'пр. Мира, д. 8, кв. 45', 'Без васаби'),
       (5, 3, 5, 'ON_THE_WAY', 1180.00, 'ул. Лесная, д. 3', 'Нужны салфетки');

-- 5.6 Позиции в заказах
INSERT INTO order_items (order_id, menu_item_id, quantity, price_at_order_time, special_instructions)
VALUES
-- Заказ 1
(1, 1, 1, 450.00, 'Дополнительно сыр'),
(1, 3, 1, 480.00, 'Аль денте'),
-- Заказ 2
(2, 5, 2, 320.00, NULL),
-- Заказ 3
(3, 9, 2, 590.00, 'Больше сыра');

-- 5.7 Платежи
INSERT INTO payments (order_id, amount, status, payment_method, external_payment_id)
VALUES (1, 1000.00, 'SUCCEEDED', 'CARD', 'pay_123456789'),
       (2, 600.00, 'SUCCEEDED', 'CARD_ONLINE', 'pay_987654321'),
       (3, 1180.00, 'PENDING', 'CASH', NULL);

-- 5.8 Отзывы
INSERT INTO reviews (order_id, customer_id, restaurant_rating, courier_rating, comment_text)
VALUES (1, 5, 5, 4, 'Вкусная пицца, доставили быстро!');

-- 5.9 Геолокация курьеров
INSERT INTO courier_locations (courier_id, latitude, longitude)
VALUES (4, 55.751244, 37.618423),
       (5, 55.754932, 37.621512);

-- 5.10 Настройки уведомлений
INSERT INTO notification_preferences (user_id, channel, notification_type, is_enabled)
VALUES (5, 'EMAIL', 'ORDER_STATUS_CHANGED', TRUE),
       (5, 'PUSH', 'ORDER_STATUS_CHANGED', TRUE),
       (6, 'EMAIL', 'PROMOTION', FALSE),
       (6, 'SMS', 'ORDER_STATUS_CHANGED', TRUE);

-- 5.11 События пользователей (для аналитики)
INSERT INTO user_events (user_id, event_type, entity_id, event_data)
VALUES (5, 'VIEW_RESTAURANT', '1', '{
  "view_duration_seconds": 45
}'),
       (5, 'ADD_TO_CART', '1', '{
         "item_id": 1,
         "quantity": 2
       }'),
       (6, 'SEARCH', NULL, '{
         "query": "суши",
         "results_count": 12
       }'),
       (6, 'PLACE_ORDER', '2', '{
         "total_amount": 600
       }');

-- ============================================
-- 6. ПРОВЕРОЧНЫЕ SELECT-ЗАПРОСЫ
-- ============================================

-- Проверим количество записей в таблицах
SELECT 'users' as table_name, COUNT(*) as count
FROM users
UNION ALL
SELECT 'restaurants', COUNT(*)
FROM restaurants
UNION ALL
SELECT 'menu_items', COUNT(*)
FROM menu_items
UNION ALL
SELECT 'orders', COUNT(*)
FROM orders
UNION ALL
SELECT 'order_items', COUNT(*)
FROM order_items
UNION ALL
SELECT 'payments', COUNT(*)
FROM payments
ORDER BY table_name;

-- Посмотрим на все заказы с информацией
SELECT o.order_id,
       c.full_name as customer,
       r.name      as restaurant,
       o.status,
       o.total_amount,
       o.created_at
FROM orders o
         JOIN users c ON o.customer_id = c.user_id
         JOIN restaurants r ON o.restaurant_id = r.restaurant_id
ORDER BY o.created_at DESC;

-- Посмотрим меню определенного ресторана
SELECT r.name  as restaurant,
       mc.name as category,
       mi.name as item,
       mi.price,
       mi.is_available
FROM menu_items mi
         JOIN menu_categories mc ON mi.category_id = mc.category_id
         JOIN restaurants r ON mc.restaurant_id = r.restaurant_id
WHERE r.restaurant_id = 1
ORDER BY mc.sort_order, mi.name;