-- Database: ordering_system_db

-- DROP DATABASE ordering_system_db;

CREATE DATABASE ordering_system_db
WITH
OWNER = postgres
ENCODING = 'UTF8'
LC_COLLATE = 'Russian_Russia.1251'
LC_CTYPE = 'Russian_Russia.1251'
TABLESPACE = pg_default
CONNECTION LIMIT = -1;

CREATE TABLE items (
  item_id     SERIAL PRIMARY KEY,
  category_id INT          NOT NULL,
  state_id    INT          NOT NULL,
  item_name   VARCHAR(100) NOT NULL,
  item_cost   MONEY
);

CREATE TABLE attachments (
  attachment_id SERIAL PRIMARY KEY,
  item_id       INT REFERENCES items (item_id),
  name          VARCHAR(100) NOT NULL,
  attached      TIMESTAMP,
  description   VARCHAR(1000)
);

CREATE TABLE categories (
  category_id SERIAL PRIMARY KEY,
  name        VARCHAR(100) NOT NULL,
  description VARCHAR(1000)
);

ALTER TABLE items
  ADD FOREIGN KEY (category_id) REFERENCES categories (category_id);

CREATE TABLE states (
  state_id       SERIAL PRIMARY KEY,
  name           VARCHAR(100) NOT NULL,
  latest_changes TIMESTAMP,
  description    VARCHAR(1000)
);

ALTER TABLE items
  ADD FOREIGN KEY (state_id) REFERENCES states (state_id);

CREATE TABLE comments (
  comment_id SERIAL PRIMARY KEY,
  item_id    INT REFERENCES items (item_id),
  topic      VARCHAR(100)  NOT NULL,
  created    TIMESTAMP,
  content    VARCHAR(1000) NOT NULL
);

CREATE TABLE users (
  user_id   SERIAL PRIMARY KEY,
  role_id   INT NOT NULL,
  user_name VARCHAR(100)
);

CREATE TABLE roles (
  role_id   SERIAL PRIMARY KEY,
  role_name VARCHAR(100)
);

ALTER TABLE users
  ADD FOREIGN KEY (role_id) REFERENCES roles (role_id);

CREATE TABLE permissions (
  permission_id SERIAL PRIMARY KEY,
  name          VARCHAR(100),
  description   VARCHAR(1000)
);

CREATE TABLE roles_permissions (
  id            SERIAL PRIMARY KEY,
  role_id       INT NOT NULL REFERENCES roles (role_id),
  permission_id INT NOT NULL REFERENCES permissions (permission_id)
);

INSERT INTO roles (role_name) VALUES ('admin');
INSERT INTO roles (role_name) VALUES ('superuser');
INSERT INTO roles (role_name) VALUES ('support');
INSERT INTO roles (role_name) VALUES ('user');
INSERT INTO roles (role_name) VALUES ('guest');

INSERT INTO permissions (name, description) VALUES ('644', '-rw-r--r--');
INSERT INTO permissions (name, description) VALUES ('660', '-rw-rw----');
INSERT INTO permissions (name, description) VALUES ('664', '-rw-rw-r--');
INSERT INTO permissions (name, description) VALUES ('700', '-rwx------');
INSERT INTO permissions (name, description) VALUES ('755', '-rwxr-xr-x');

INSERT INTO categories (name) VALUES ('urgent');
INSERT INTO categories (name) VALUES ('usual');
INSERT INTO categories (name) VALUES ('pending');
INSERT INTO categories (name) VALUES ('cancelled');
INSERT INTO categories (name) VALUES ('regular');

INSERT INTO states (name, description) VALUES ('new order', 'new order desc');
INSERT INTO states (name, description) VALUES ('preparing', 'order preparation desc');
INSERT INTO states (name, description) VALUES ('shipment', 'order shipment desc');
INSERT INTO states (name, description) VALUES ('completed', 'order completed');

INSERT INTO users (role_id, user_name) VALUES (10, 'administrator');
INSERT INTO users (role_id, user_name) VALUES (13, 'some user');
INSERT INTO users (role_id, user_name) VALUES (12, 'support engineer');
INSERT INTO users (role_id, user_name) VALUES (14, 'guest-user');

INSERT INTO roles_permissions (role_id, permission_id) VALUES (10, 19);
INSERT INTO roles_permissions (role_id, permission_id) VALUES (11, 18);
INSERT INTO roles_permissions (role_id, permission_id) VALUES (12, 17);
INSERT INTO roles_permissions (role_id, permission_id) VALUES (13, 20);
INSERT INTO roles_permissions (role_id, permission_id) VALUES (14, 20);

INSERT INTO items (category_id, state_id, item_name, item_cost) VALUES (16, 13, 'first item', 500.0);
INSERT INTO items (category_id, state_id, item_name, item_cost) VALUES (20, 14, 'second item', 777.7);
INSERT INTO items (category_id, state_id, item_name, item_cost) VALUES (19, 15, 'third item', 1.0);
INSERT INTO items (category_id, state_id, item_name, item_cost) VALUES (17, 16, 'fourth item', 1011.0);

INSERT INTO attachments (item_id, name, attached, description)
VALUES (1, 'some picture', '2019-02-07', 'attached picture');
INSERT INTO attachments (item_id, name, attached, description)
VALUES (0, 'some text file', '2019-02-06', 'attached text file');
INSERT INTO attachments (item_id, name, attached, description)
VALUES (3, 'some video', '2019-02-05', 'attached video');
INSERT INTO attachments (item_id, name, attached, description)
VALUES (2, 'some .class file', '2019-02-04', 'attached .class file');

INSERT INTO comments (item_id, topic, created, content)
VALUES (2, 'discount', '2019-02-08', 'the item costs too much');
INSERT INTO comments (item_id, topic, created, content)
VALUES (4, 'delivery time', '2019-02-07', 'when this item will arrive to Australia?');
INSERT INTO comments (item_id, topic, created, content)
VALUES (5, 'reorder', '2019-02-06', 'the order cancelled by accident');


