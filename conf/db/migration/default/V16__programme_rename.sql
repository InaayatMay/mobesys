alter table course_information
modify column programme varchar(100);

update course_information
set programme = 'Bachelor of Computer Science (Hons)'
where programme = 'Bachelor of Computer Science';

update course_information
set programme = 'Bachelor of Computer Engineering (Hons)'
where department_id = 1 and programme = 'Bachelor of Engineering (Hons)';