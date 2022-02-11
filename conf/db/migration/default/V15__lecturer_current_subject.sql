create table IF NOT EXISTS lecturer_current_subject(
	id SERIAL PRIMARY KEY,
    lecturer_id INT REFERENCES lecturer(id),
    course_information_id INT references course_information(id),
    course_name varchar(100),
    is_completed boolean,
    current_page varchar(50)
);