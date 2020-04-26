create table user (
    id int not null auto_increment,
    username varchar,
    password varchar
);

create table role (
    id int not null auto_increment,
    name varchar
);

create table user_role (
    id int not null auto_increment,
    user_id int,
    role_id int
);
