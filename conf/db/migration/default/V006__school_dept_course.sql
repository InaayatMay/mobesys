CREATE TABLE IF NOT EXISTS school(
     id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
     school_name VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS department(
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    department_name VARCHAR(100),
    school_id INT REFERENCES school(id)
);

--ALTER TABLE course_information
--DROP COLUMN lecturer_id;

ALTER TABLE course_information
ADD COLUMN department_id INT REFERENCES department(id);

INSERT INTO school(school_name)
VALUES
('SCHOOL_1'),
('SCHOOL_2');

INSERT INTO department(department_name, school_id)
VALUES
('DEPARTMENT_1', 1),
('DEPARTMENT_2', 1),
('DEPARTMENT_1', 2),
('DEPARTMENT_2', 2);

DELETE
FROM course_information;

INSERT INTO course_information(programme, course_code, course_name, semester, intake_batch, course_type, department_id)
VALUES
('PROGRAMME_1', 'COURSE_CODE_1', 'COURSE_NAME_A', 1, 'JAN-10', 'THEORY', 1),
('PROGRAMME_2', 'COURSE_CODE_2', 'COURSE_NAME_A', 2, 'FEB-10', 'THEORY', 2),
('PROGRAMME_3', 'COURSE_CODE_3', 'COURSE_NAME_A', 3, 'MAR-10', 'THEORY', 3),
('PROGRAMME_4', 'COURSE_CODE_4', 'COURSE_NAME_A', 4, 'APR-10', 'THEORY', 4);