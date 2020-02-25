create sequence hibernate_sequence;

create table company (
    id bigint not null auto_increment,
    name varchar,
    sector int
);

create table employee (
    id bigint not null auto_increment,
    company_id int,
    firstname varchar,
    lastname varchar,
    birthday date
);

create table company_employee (
    id bigint not null auto_increment,
    company_id bigint,
    employee_id bigint
);

