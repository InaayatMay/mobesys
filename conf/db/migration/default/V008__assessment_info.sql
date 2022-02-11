CREATE TABLE assessment_info (
    id serial PRIMARY KEY,
    assessment VARCHAR(20),
    assessment_type VARCHAR(30),
    full_marks INT,
    weightage INT,
    clo_title VARCHAR(10),
    lecturer_id INT REFERENCES lecturer(id),
    course_information_id INT REFERENCES course_information(id)
);