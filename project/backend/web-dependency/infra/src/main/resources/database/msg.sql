drop table if exists user_ip;
create table user_ip
(
    uid bigint not null,
    ip  varchar not null,
    primary key (uid,ip)
);
drop table if exists msg_unposed;
create table msg_unposed
(
    message_id   int8 primary key,
    from_id      int8 not null,
    to_id        int8 not null,
    type         int4 not null,
    required_ack bool not null,
    body         json
);
drop table if exists msg_unacked;
create table msg_unacked
(
    like msg_unposed including all
);