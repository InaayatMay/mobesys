CREATE TABLE IF NOT EXISTS student_course_map(
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    student_id int REFERENCES student(id),
    course_information_id int REFERENCES course_information(id)
);

ALTER TABLE student
DROP COLUMN course_information_id;

