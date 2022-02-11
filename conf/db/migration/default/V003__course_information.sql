CREATE TABLE course_information(
    id serial PRIMARY KEY,
    programme VARCHAR(30),
    course_code VARCHAR(30),
    course_name VARCHAR(50),
    semester VARCHAR(10),
    intake_batch VARCHAR(30),
    lecturer_id INT REFERENCES lecturer(id),
    course_type VARCHAR(30)
);