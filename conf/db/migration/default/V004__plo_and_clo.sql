CREATE TABLE programme_learning_outcome(
    id serial PRIMARY KEY,
    title VARCHAR(100),
    code VARCHAR(5),
    course_information_id INT REFERENCES course_information(id),
    lecturer_id INT REFERENCES lecturer(id)
);

CREATE TABLE course_learning_outcome(
    id serial PRIMARY KEY,
    title VARCHAR(30),
    plo_code VARCHAR(5),
    course_information_id INT REFERENCES course_information(id),
    lecturer_id INT REFERENCES lecturer(id)
);