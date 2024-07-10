create schema if not exists user_management;

create table user_management.t_user(
    id serial primary key,
    c_username varchar not null check (length(trim(c_username)) > 0) unique,
    c_password varchar
);

create table user_management.t_authority(
    id serial primary key,
    c_authority varchar
);

create table user_management.t_user_authority(
    id serial primary key,
    id_user int references user_management.t_user(id),
    id_authority int references user_management.t_authority(id),
    constraint uk_user_authority unique (id_user, id_authority)
);