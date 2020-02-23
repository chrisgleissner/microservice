insert into company (id, name, sector) values (1, 'Foo', 0);
insert into company (id, name, sector) values (2, 'Bar', 1);

insert into employee (id, firstname, lastname, birthday) values (1, 'John', 'Doe', '1970-01-01');
insert into employee (id, firstname, lastname, birthday) values (2, 'Jill', 'Zoe', '1971-01-01');
insert into employee (id, firstname, lastname, birthday) values (3, 'Jane', 'Doe', '1971-01-01');

insert into company_employee (id, company_id, employee_id) values (1, 1, 1);
insert into company_employee (id, company_id, employee_id) values (2, 1, 2);
insert into company_employee (id, company_id, employee_id) values (3, 2, 3);
