CREATE TABLE lecturer(
     id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
     first_name VARCHAR(50),
     last_name VARCHAR(50),
     gender VARCHAR(6),
     code_number VARCHAR(15),
     subject_code VARCHAR(20),
     email VARCHAR(50),
     password VARCHAR(20)
);

CREATE TABLE student(
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    gender VARCHAR(6),
    code_number VARCHAR(15),
    program VARCHAR(50),
    current_semester INT,
    email VARCHAR(50)
);

CREATE TABLE lecturer_student_subject(
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    subject_name VARCHAR(50),
    subject_code VARCHAR(30),
    lecturer_id INT REFERENCES lecturer(id),
    student_id INT REFERENCES student(id)
);
