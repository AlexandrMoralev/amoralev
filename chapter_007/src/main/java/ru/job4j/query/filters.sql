CREATE TABLE type (
  id   SERIAL PRIMARY KEY,
  name VARCHAR(30)
);

CREATE TABLE product (
  id           SERIAL PRIMARY KEY,
  name         VARCHAR(20) NOT NULL,
  type_id      INT         NOT NULL REFERENCES type (id),
  expired_date TIMESTAMP   NOT NULL,
  price        INT
);

INSERT INTO type (name) VALUES ('СЫР');
INSERT INTO type (name) VALUES ('МОЛОКО');
INSERT INTO type (name) VALUES ('МЯСО');
INSERT INTO type (name) VALUES ('ОВОЩИ');
INSERT INTO type (name) VALUES ('ХИМИЯ');

INSERT INTO product (name, type_id, expired_date, price) VALUES ('молоко', 2, '2019-02-09', 98);
INSERT INTO product (name, type_id, expired_date, price) VALUES ('сырный продукт', 1, '2023-11-22', 700);
INSERT INTO product (name, type_id, expired_date, price) VALUES ('свиная отбивная', 3, '2019-03-01', 500);
INSERT INTO product (name, type_id, expired_date, price) VALUES ('томаты "мутант"', 4, '2019-01-01', 320);
INSERT INTO product (name, type_id, expired_date, price) VALUES ('мороженое', 5, '2019-03-09', 20);

-- 1 Написать запрос получение всех продуктов с типом "СЫР"

SELECT *
FROM product
WHERE type_id = (SELECT id
                 FROM type
                 WHERE type.name = 'СЫР');

-- 2  Написать запрос получения всех продуктов, у кого в имени есть слово "мороженное"

SELECT *
FROM product
WHERE name LIKE '%мороженое%';

-- 3  Написать запрос, который выводит все продукты, срок годности которых заканчивается в следующем месяце.

SELECT *
FROM product
WHERE expired_date
BETWEEN '2019-02-08' :: DATE
AND ('2019-02-08' :: DATE + '1 month' :: INTERVAL);

-- 4 Написать запрос, который выводит самый дорогой продукт.

SELECT
  id,
  name,
  expired_date
FROM product
ORDER BY price DESC
LIMIT 1;

    -- или
SELECT *
FROM product
WHERE price = (SELECT MAX(price)
               FROM product);

-- 5 Написать запрос, который выводит количество всех продуктов определенного типа.

SELECT count(product.type_id)
FROM product
  INNER JOIN type t ON product.type_id = t.id AND t.name = 'СЫР';

-- 6 Написать запрос получение всех продуктов с типом "СЫР" и "МОЛОКО"

SELECT *
FROM product
WHERE type_id IN
      (SELECT id
       FROM type
       WHERE type.name = 'СЫР' OR type.name = 'МОЛОКО');

-- 7 Написать запрос, который выводит тип продуктов, которых осталось меньше 10 штук.

SELECT type.name
FROM type
  INNER JOIN product p ON type.id = p.type_id
                          AND (SELECT count(product.type_id)
                               FROM product) < 10;

-- 8 Вывести все продукты и их тип.

SELECT
  product.name,
  type.name
FROM product
  INNER JOIN type ON product.type_id = type.id;

