alter table lecturer add column phone_number text;
alter table lecturer add column birth_month int;
alter table lecturer add column birth_day int;
alter table lecturer add column birth_year int;

update lecturer
set birth_month = 1
where id = 1;

update lecturer
set birth_month = 1
where id = 2;

update lecturer
set birth_month = 1
where id = 3;

update lecturer
set birth_day = 1
where id = 1;

update lecturer
set birth_day = 1
where id = 2;

update lecturer
set birth_day = 1
where id = 3;

update lecturer
set birth_year = 1940
where id = 1;

update lecturer
set birth_year = 1940
where id = 2;

update lecturer
set birth_year = 1940
where id = 3;