CREATE TABLE lecturer(
     id serial PRIMARY KEY,
     first_name VARCHAR(50),
     last_name VARCHAR(50),
     gender VARCHAR(6),
     code_number VARCHAR(15),
     subject_code VARCHAR(20),
     email VARCHAR(50),
     password VARCHAR(20)
);

CREATE TABLE student(
    id serial PRIMARY KEY,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    gender VARCHAR(6),
    code_number VARCHAR(15),
    program VARCHAR(50),
    current_semester INT,
    email VARCHAR(50)
);

CREATE TABLE lecturer_student_subject(
    id serial PRIMARY KEY,
    subject_name VARCHAR(50),
    subject_code VARCHAR(30),
    lecturer_id INT REFERENCES lecturer(id),
    student_id INT REFERENCES student(id)
);

