create table if not exists request(
id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
from_lecturer_id INT REFERENCES lecturer(id),
to_lecturer_id INT REFERENCES lecturer(id),
course_information_id INT REFERENCES course_information(id),
request text,
is_approved boolean,
created_at timestamp,
approved_at timestamp
);