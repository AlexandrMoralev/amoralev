drop table if exists hall;
drop table if exists accounts;

create table hall
(
  ticket_id  serial primary key,
  row        integer not null,
  seat       integer not null,
  price      integer not null,
  ordered    bigint default null,
  constraint ticket_unq unique(row, seat),
  constraint ticket_valid check (row > 0 and seat > 0 and price >= 0 and (ordered is null or ordered > 0))
);

create table accounts
(
  account_id serial primary key,
  fio     varchar(200) not null,
  phone   varchar(200) not null,
  constraint acc_unq unique(fio, phone)
);