CREATE TABLE IF NOT EXISTS lecturer_course_map(
    id serial PRIMARY KEY,
    lecturer_id INT REFERENCES lecturer(id),
    course_information_id INT REFERENCES course_information(id),
    department_id INT REFERENCES department(id),
    school_id INT REFERENCES school(id)
);