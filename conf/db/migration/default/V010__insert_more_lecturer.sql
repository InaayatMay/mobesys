ALTER TABLE lecturer
DROP COLUMN subject_code;

INSERT INTO lecturer(first_name, last_name, gender, code_number, email, password)
VALUES
('Dr. Fatimah Audah',	'Md. Zaki', 'Female', '100515101', 'fatimah@gmail.com', '123'),
('Dr. Ahmad Anwar', 'Zainuddin', 'Male', '100515102', 'anwar@gmail.com', '123');

DROP TABLE lecturer_student_subject;

CREATE TABLE lecturer_student_course (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    lecturer_id INT REFERENCES lecturer(id),
    course_information_id INT REFERENCES course_information(id),
    student_id INT REFERENCES student(id)
)