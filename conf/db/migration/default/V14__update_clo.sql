ALTER TABLE course_learning_outcome
ADD COLUMN code VARCHAR(10);

ALTER TABLE assessment_info
ALTER COLUMN weightage TYPE decimal;

ALTER TABLE assessment_info RENAME clo_title TO clo_code;

ALTER TABLE assessment_info ALTER COLUMN clo_code TYPE VARCHAR(200);

ALTER TABLE assessment_info
ALTER COLUMN assessment TYPE VARCHAR(100);

CREATE TABLE IF NOT exists student_marks(
    id SERIAL PRIMARY KEY,
    student_id INT references student(id),
    assessment_id INT references assessment_info(id),
    marks DECIMAL,
    lecturer_id INT REFERENCES lecturer(id),
    course_information_id INT references course_information(id)
);

CREATE TABLE student_number_of_attempt(
	id SERIAL PRIMARY KEY,
    student_id INT references student(id),
    number_of_attempt INT,
    lecturer_id INT REFERENCES lecturer(id),
    course_information_id INT references course_information(id)
);

CREATE TABLE previous_clo_record(
	id SERIAL PRIMARY KEY,
    clo_code VARCHAR(50),
    previous_semester_class_average DECIMAL,
    comments text,
    lecturer_id INT REFERENCES lecturer(id),
    course_information_id INT references course_information(id)
);