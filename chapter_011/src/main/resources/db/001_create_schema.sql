-- Создайте новую базу auto_crash в postgresql.
--
CREATE TABLE accident
(
  id serial primary key,
  name varchar(2000),
  text varchar(2000),
  address varchar(2000)
);