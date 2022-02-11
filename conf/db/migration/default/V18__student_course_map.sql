CREATE TABLE IF NOT EXISTS student_course_map(
    id SERIAL PRIMARY KEY,
    student_id int REFERENCES student(id),
    course_information_id int REFERENCES course_information(id)
);

ALTER TABLE student
DROP COLUMN course_information_id;

