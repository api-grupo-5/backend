CREATE TABLE cart_items
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    quantity   INT NOT NULL,
    product_id BIGINT NULL,
    cart_id    BIGINT NULL,
    added_on   datetime NULL,
    updated_on datetime NULL,
    CONSTRAINT pk_cart_items PRIMARY KEY (id)
);

CREATE TABLE carts
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    created_on datetime NULL,
    user_id    BIGINT NULL,
    CONSTRAINT pk_carts PRIMARY KEY (id)
);

CREATE TABLE order_items
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    order_id      BIGINT NULL,
    product_id    BIGINT NULL,
    quantity      INT NOT NULL,
    price DOUBLE NULL,
    name          VARCHAR(255) NULL,
    `description` VARCHAR(255) NULL,
    image         VARCHAR(255) NULL,
    category      VARCHAR(255) NULL,
    seller_id     BIGINT NULL,
    CONSTRAINT pk_order_items PRIMARY KEY (id)
);

CREATE TABLE orders
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    date        datetime NULL,
    amount DOUBLE NOT NULL,
    customer_id BIGINT NULL,
    cart_id     BIGINT NULL,
    CONSTRAINT pk_orders PRIMARY KEY (id)
);

CREATE TABLE otp_tokens
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    token      VARCHAR(255) NOT NULL,
    expiration datetime     NOT NULL,
    user_id    BIGINT       NOT NULL,
    username   VARCHAR(255) NOT NULL,
    CONSTRAINT pk_otp_tokens PRIMARY KEY (id)
);

CREATE TABLE products
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    name          VARCHAR(255) NULL,
    `description` VARCHAR(255) NULL,
    price DOUBLE NULL,
    stock         INT NOT NULL,
    image         VARCHAR(255) NULL,
    category      VARCHAR(255) NULL,
    seller_id     BIGINT NULL,
    CONSTRAINT pk_products PRIMARY KEY (id)
);

CREATE TABLE roles
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    name        VARCHAR(255) NULL,
    permissions VARCHAR(255) NULL,
    CONSTRAINT pk_roles PRIMARY KEY (id)
);

CREATE TABLE ticket_status
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    `description` VARCHAR(255) NULL,
    CONSTRAINT pk_ticket_status PRIMARY KEY (id)
);

CREATE TABLE tickets
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    sender_id     BIGINT NOT NULL,
    title         VARCHAR(255) NULL,
    `description` VARCHAR(255) NULL,
    created_at    datetime NULL,
    updated_at    datetime NULL,
    closed_at     datetime NULL,
    image         VARCHAR(255) NULL,
    attendant_id  BIGINT NOT NULL,
    status        BIGINT NOT NULL,
    CONSTRAINT pk_tickets PRIMARY KEY (id)
);

CREATE TABLE token
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    user         BIGINT NOT NULL,
    public_token VARCHAR(255) NULL,
    expires_at   datetime NULL,
    CONSTRAINT pk_token PRIMARY KEY (id)
);

CREATE TABLE users
(
    id             BIGINT AUTO_INCREMENT NOT NULL,
    email          VARCHAR(255) NULL,
    password       VARCHAR(255) NULL,
    registered_on  datetime NULL,
    last_logged_in datetime NULL,
    role_id        BIGINT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

CREATE TABLE users_information
(
    user_id     BIGINT NOT NULL,
    first_name  VARCHAR(255) NULL,
    last_name   VARCHAR(255) NULL,
    personal_id INT    NOT NULL,
    phone       VARCHAR(255) NULL,
    address     VARCHAR(255) NULL,
    email       VARCHAR(255) NULL,
    CONSTRAINT pk_users_information PRIMARY KEY (user_id)
);

ALTER TABLE carts
    ADD CONSTRAINT uc_carts_user UNIQUE (user_id);

ALTER TABLE carts
    ADD CONSTRAINT FK_CARTS_ON_USER FOREIGN KEY (user_id) REFERENCES users_information (user_id);

ALTER TABLE cart_items
    ADD CONSTRAINT FK_CART_ITEMS_ON_CART FOREIGN KEY (cart_id) REFERENCES carts (id);

ALTER TABLE cart_items
    ADD CONSTRAINT FK_CART_ITEMS_ON_PRODUCT FOREIGN KEY (product_id) REFERENCES products (id);

ALTER TABLE orders
    ADD CONSTRAINT FK_ORDERS_ON_CART FOREIGN KEY (cart_id) REFERENCES carts (id);

ALTER TABLE orders
    ADD CONSTRAINT FK_ORDERS_ON_CUSTOMER FOREIGN KEY (customer_id) REFERENCES users_information (user_id);

ALTER TABLE order_items
    ADD CONSTRAINT FK_ORDER_ITEMS_ON_ORDER FOREIGN KEY (order_id) REFERENCES orders (id);

ALTER TABLE order_items
    ADD CONSTRAINT FK_ORDER_ITEMS_ON_PRODUCT FOREIGN KEY (product_id) REFERENCES products (id);

ALTER TABLE order_items
    ADD CONSTRAINT FK_ORDER_ITEMS_ON_SELLER FOREIGN KEY (seller_id) REFERENCES users_information (user_id);

ALTER TABLE otp_tokens
    ADD CONSTRAINT FK_OTP_TOKENS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE products
    ADD CONSTRAINT FK_PRODUCTS_ON_SELLER FOREIGN KEY (seller_id) REFERENCES users (id);

ALTER TABLE tickets
    ADD CONSTRAINT FK_TICKETS_ON_ATTENDANT FOREIGN KEY (attendant_id) REFERENCES users (id);

ALTER TABLE tickets
    ADD CONSTRAINT FK_TICKETS_ON_SENDER FOREIGN KEY (sender_id) REFERENCES users (id);

ALTER TABLE tickets
    ADD CONSTRAINT FK_TICKETS_ON_STATUS FOREIGN KEY (status) REFERENCES ticket_status (id);

ALTER TABLE token
    ADD CONSTRAINT FK_TOKEN_ON_USER FOREIGN KEY (user) REFERENCES users (id);

ALTER TABLE users_information
    ADD CONSTRAINT FK_USERS_INFORMATION_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE users
    ADD CONSTRAINT FK_USERS_ON_ROLE FOREIGN KEY (role_id) REFERENCES roles (id);