drop table if exists comments;
drop table if exists items;

create table items
(
  item_id     serial primary key,
  name        varchar(2000),
  description text,
  created     timestamp without time zone
);

create table comments
(
  comment_id serial primary key,
  items_id   int not null references items (item_id) on delete cascade,
  comment    varchar(2000),
  created    timestamp without time zone
);
