ALTER TABLE course_learning_outcome
ADD COLUMN code VARCHAR(10);

ALTER TABLE assessment_info
MODIFY weightage decimal;

ALTER TABLE assessment_info CHANGE clo_title clo_code VARCHAR(100);

ALTER TABLE assessment_info
MODIFY assessment VARCHAR(100);

CREATE TABLE IF NOT exists student_marks(
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    student_id INT references student(id),
    assessment_id INT references assessment_info(id),
    marks double,
    lecturer_id INT REFERENCES lecturer(id),
    course_information_id INT references course_information(id)
);

CREATE TABLE student_number_of_attempt(
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    student_id INT references student(id),
    number_of_attempt INT,
    lecturer_id INT REFERENCES lecturer(id),
    course_information_id INT references course_information(id)
);

CREATE TABLE previous_clo_record(
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    clo_code VARCHAR(50),
    previous_semester_class_average double,
    comments text,
    lecturer_id INT REFERENCES lecturer(id),
    course_information_id INT references course_information(id)
);