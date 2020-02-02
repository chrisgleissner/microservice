create table company (
    id int not null auto_increment,
    name varchar,
    sector int
);

create table employee (
    id int not null auto_increment,
    company_id int,
    firstname varchar,
    lastname varchar,
    birthday date
);

create table company_employee (
    id int not null auto_increment,
    company_id int,
    employee_id int
)

