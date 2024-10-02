-- write the DDL at here
-- please ensure immutability ( drop if exists then create )
drop table if exists users;
create table users(
    id bigserial primary key
);
