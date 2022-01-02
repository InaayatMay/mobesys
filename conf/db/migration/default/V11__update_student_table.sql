DROP TABLE lecturer_student_course;

ALTER TABLE student
ADD COLUMN lecturer_id INT REFERENCES lecturer(id);

ALTER TABLE student
ADD COLUMN course_information_id INT REFERENCES course_information(id);