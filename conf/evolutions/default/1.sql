# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table bans (
  id                            bigint auto_increment not null,
  ip_address                    varchar(255) not null,
  from_date                     bigint not null,
  to_date                       bigint not null,
  reason                        varchar(255),
  created_timestamp             bigint not null,
  constraint uq_bans_ip_address unique (ip_address),
  constraint pk_bans primary key (id)
);

create table posts (
  id                            bigint auto_increment not null,
  author                        varchar(255),
  content                       varchar(255) not null,
  ip_address                    varchar(255),
  user_id                       bigint,
  created_timestamp             bigint not null,
  constraint pk_posts primary key (id)
);

create table users (
  id                            bigint auto_increment not null,
  username                      varchar(255),
  password                      varchar(255) not null,
  active                        boolean default false not null,
  role                          varchar(255),
  created_at                    bigint not null,
  constraint uq_users_username unique (username),
  constraint pk_users primary key (id)
);

create index ix_posts_user_id on posts (user_id);
alter table posts add constraint fk_posts_user_id foreign key (user_id) references users (id) on delete restrict on update restrict;


# --- !Downs

alter table posts drop constraint if exists fk_posts_user_id;
drop index if exists ix_posts_user_id;

drop table if exists bans;

drop table if exists posts;

drop table if exists users;

