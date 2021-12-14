ALTER TABLE programme_learning_outcome
DROP COLUMN course_information_id;

ALTER TABLE programme_learning_outcome
DROP COLUMN lecturer_id;

DELETE
FROM programme_learning_outcome;

INSERT INTO programme_learning_outcome(title, code)
VALUES ('[WA1]Engineering Knowledge', 'PLO1'),
('[WA2]Problem Analysis', 'PLO2'),
('[WA3]Design / Development of Solutions', 'PLO3'),
('[WA4]Investigation', 'PLO4'),
('[WA5]Modern Tool Usage', 'PLO5'),
('[WA6]The Engineer and Society', 'PLO6'),
('[WA7]Environment and Sustainability', 'PLO7'),
('[WA8]Ethics', 'PLO8'),
('[WA9]Individual & Team Work', 'PLO9'),
('[WA10]Communication', 'PLO10'),
('[WA11]Project Management & Finance', 'PLO11'),
('[WA12]Life-Long Learning', 'PLO12');