truncate table hall;
truncate table accounts;

INSERT INTO hall (row, seat, price)
VALUES (1, 1, 400),
       (1, 2, 500),
       (1, 3, 400),
       (2, 1, 300),
       (2, 2, 400),
       (2, 3, 300),
       (3, 1, 200),
       (3, 2, 300),
       (3, 3, 200);

INSERT INTO accounts (fio, phone) VALUES ('ROOT', '+7(000)000-00-00');
