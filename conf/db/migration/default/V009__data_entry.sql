ALTER TABLE course_information
ALTER COLUMN course_name TYPE VARCHAR(100);

INSERT INTO school(id, school_name) VALUES(1, 'School of Engineering and Computing');

INSERT INTO department(id, department_name, school_id)
VALUES
(1, 'Department of Computer Engineering and Computer Science', 1),
(2, 'Department of Civil Engineering', 1);

INSERT INTO course_information(id, programme, course_code, course_name, semester, intake_batch, course_type, department_id)
VALUES
(1, 'Bachelor of Computer Science', 'SCB 1003', 'Probability and Statistics', 1, 'Oct-21', 'Theory(only)', 1),
(2, 'Bachelor of Computer Science', 'SCB 1024', 'Computer Programming with Lab', 1, 'Oct-21', 'Theory with Lab', 1),
(3, 'Bachelor of Computer Science', 'UTB 1062', 'Self-Development & Leadership Buildings', 1, 'Oct-21', 'Theory(only)', 1),
(4, 'Bachelor of Computer Science', 'UTB 1053', 'English for Professional Communication', 1, 'Oct-21', 'Theory(only)', 1),
(5, 'Bachelor of Computer Science', 'SCB 1033', 'Discrete Mathematics', 1, 'Oct-21', 'Theory(only)', 1),

(6, 'Bachelor of Computer Science', 'SCB 1054', 'Internet & Web Programming with Lab', 2, 'March 2021', 'Theory with Lab', 1),
(7, 'Bachelor of Computer Science', 'SCB 1064', 'Database Management System with Lab', 2, 'March 2021', 'Theory with Lab', 1),
(8, 'Bachelor of Computer Science', 'UOB 3362', 'Introduction to Organizational Behavior', 2, 'March 2021', 'Theory(only)', 1),
(9, 'Bachelor of Computer Science', 'SCB 1074', 'Object Oriented Programming with Lab', 2, 'March 2021', 'Theory with Lab', 1),
(10, 'Bachelor of Computer Science', 'SCB 1083', 'Computer Organization and Architecture', 2, 'March 2021', 'Theory(only)', 1),

(11, 'Bachelor of Computer Science', 'SCB 2093', 'Software Engineering', 3, 'AUGUST & OCT 2020', 'Theory(only)', 1),
(12, 'Bachelor of Computer Science', 'SCB 2104', 'Operating System', 3, 'AUGUST & OCT 2020', 'Theory(only)', 1),
(13, 'Bachelor of Computer Science', 'SCB 2114', 'Data Structures with Lab', 3, 'AUGUST & OCT 2020', 'Theory with Lab', 1),
(14, 'Bachelor of Computer Science', 'SCB 2124', 'Computer Network with Lab', 3, 'AUGUST & OCT 2020', 'Theory with Lab', 1),

(15, 'Bachelor of Computer Science', 'SCB 2183', 'Introduction to Multimedia', 4, 'March 2020', 'Theory(only)', 1),
(16, 'Bachelor of Computer Science', 'SCB 3263', 'Elective 1 (Data Warehousing & Data Mining)', 4, 'March 2020', 'Theory(only)', 1),
(17, 'Bachelor of Computer Science', 'SCB 3343', 'Elective 2 (Cloud Computing)', 4, 'March 2020', 'Theory(only)', 1),
(18, 'Bachelor of Computer Science', 'SCB 2143', 'Human Computer Interaction', 4, 'March 2020', 'Theory(only)', 1),
(19, 'Bachelor of Computer Science', 'SCB 2154', 'Serverside Programming with Lab', 4, 'March 2020', 'Theory with Lab', 1),
(20, 'Bachelor of Computer Science', 'SCB 1042', 'Professional Computing Practices', 4, 'March 2020', 'Theory(only)', 1),

(21, 'Bachelor of Computer Science', 'SCB 3203', 'Artificial Intelligence', 5, 'AUGUST & OCTOBER 2019', 'Theory(only)', 1),
(22, 'Bachelor of Computer Science', 'SCB 3173', 'Cryptography and Network Security', 5, 'AUGUST & OCTOBER 2019', 'Theory(only)', 1),
(23, 'Bachelor of Computer Science', 'SCB 3374', 'Data Science Tools [Python programming with lab]', 5, 'AUGUST & OCTOBER 2019', 'Theory with Lab', 1),
(24, 'Bachelor of Computer Science', 'EKB 3192', 'Project Management', 5, 'AUGUST & OCTOBER 2019', 'Theory(only)', 1),
(25, 'Bachelor of Computer Science', 'SCB 3243', 'Elective 3(Big Data)', 5, 'AUGUST & OCTOBER 2019', 'Theory(only)', 1),
(26, 'Bachelor of Computer Science', 'SCB 3192', 'Final Year Project 1', 5, 'AUGUST & OCTOBER 2019', 'Theory(only)', 1),

(27, 'Bachelor of Computer Science', 'SCB 3224', 'Software Testing & Quality Assurance with Lab', 6, 'March 2019', 'Theory with Lab', 1),
(28, 'Bachelor of Computer Science', 'SCB 3233', 'Machine Learning', 6, 'March 2019', 'Theory(only)', 1),
(29, 'Bachelor of Computer Science', 'SCB 3303', 'Elective 4 [Mobile Computing]', 6, 'March 2019', 'Theory(only)', 1),
(30, 'Bachelor of Computer Science', 'SCB 3214', 'Final Year Project 2', 6, 'March 2019', 'Theory(only)', 1),
(31, 'Bachelor of Computer Science', 'UEB 2542', 'Entrepreneurship', 6, 'March 2019', 'Theory(only)', 1),
(32, 'Bachelor of Computer Science', 'SCB 3362', 'Professional Learning', 6, 'March 2019', 'Theory(only)', 1),

(33, 'Bachelor of Engineering (Hons)', 'EYB 1013', 'Engineering Mathematics 1', 1, 'Oct-21', 'Theory(only)', 1),
(34, 'Bachelor of Engineering (Hons)', 'EYB 1023', 'Computer Programming', 1, 'Oct-21', 'Theory(only)', 1),
(35, 'Bachelor of Engineering (Hons)', 'EYB 1031', 'Computer Programming Lab', 1, 'Oct-21', 'Theory with Lab', 1),
(36, 'Bachelor of Engineering (Hons)', 'EYB 1043', 'Engineering Graphics', 1, 'Oct-21', 'Theory(only)', 1),
(37, 'Bachelor of Engineering (Hons)', 'USB 1062', 'Self-Development & Leadership Building', 1, 'Oct-21', 'Theory(only)', 1),
(38, 'Bachelor of Engineering (Hons)', 'UTB 1053', 'English for Professional Communication', 1, 'Oct-21', 'Theory(only)', 1),
(39, 'Bachelor of Engineering (Hons)', 'MPU 3163', 'Bahasa Melayu Komunikasi 2', 1, 'Oct-21', 'Theory(only)', 1),
(40, 'Bachelor of Engineering (Hons)', 'MPU 3113', 'Hubungan Etnik', 1, 'Oct-21', 'Theory(only)', 1),

(41, 'Bachelor of Engineering (Hons)', 'EYB 1083', 'Engineering Maths 2', 2, 'Mar-21', 'Theory(only)', 1),
(42, 'Bachelor of Engineering (Hons)', 'ECB 1103', 'Internet & Web Programming', 2, 'Mar-21', 'Theory(only)', 1),
(43, 'Bachelor of Engineering (Hons)', 'ECB 1111', 'Internet & Web Programming Lab', 2, 'Mar-21', 'Theory with Lab', 1),
(44, 'Bachelor of Engineering (Hons)', 'EEB 1113', 'Elements of Electrical Engineering', 2, 'Mar-21', 'Theory(only)', 1),
(45, 'Bachelor of Engineering (Hons)', 'EEB 1121', 'Fundamentals of Electrical and Electronics Lab', 2, 'Mar-21', 'Theory with Lab', 1),
(46, 'Bachelor of Engineering (Hons)', 'EEB 1213', 'Electronics Devices & Circuits', 2, 'Mar-21', 'Theory(only)', 1),
(47, 'Bachelor of Engineering (Hons)', 'EEB 1221', 'Electonics Circuit Lab', 2, 'Mar-21', 'Theory with Lab', 1),
(48, 'Bachelor of Engineering (Hons)', 'EYB 1022', 'Environmental Science', 2, 'Mar-21', 'Theory(only)', 1),

(49, 'Bachelor of Engineering (Hons)', 'EEB 1233', 'Combinational & Sequential Circuits', 3, 'AUGUST & OCT 2020', 'Theory(only)', 1),
(50, 'Bachelor of Engineering (Hons)', 'ECB 2063 ', 'Data Structures', 3, 'AUGUST & OCT 2020', 'Theory(only)', 1),
(51, 'Bachelor of Engineering (Hons)', 'ECB 2071', 'Data Structures Lab', 3, 'AUGUST & OCT 2020', 'Theory with Lab', 1),
(52, 'Bachelor of Engineering (Hons)', 'ECB 2033', 'Object Oriented Programming', 3, 'AUGUST & OCT 2020', 'Theory(only)', 1),
(53, 'Bachelor of Engineering (Hons)', 'ECB 2041', 'Object Oriented Programming Lab', 3, 'AUGUST & OCT 2020', 'Theory with Lab', 1),
(54, 'Bachelor of Engineering (Hons)', 'EYB 2153', 'Engineering Maths 3', 3, 'AUGUST & OCT 2020', 'Theory(only)', 1),
(55, 'Bachelor of Engineering (Hons)', 'UEB 2542', 'Entrepreneurship', 3, 'AUGUST & OCT 2020', 'Theory(only)', 1),

(138, 'Bachelor of Engineering (Hons)', 'EEB 3143', 'Microprocessors & Microcontrollers', 4, 'March 2020', 'Theory(only)', 1),
(56, 'Bachelor of Engineering (Hons)', 'EEB 3131', 'Microprocessors Lab', 4, 'March 2020', 'Theory with Lab', 1),
(57, 'Bachelor of Engineering (Hons)', 'ECB 2253', 'Database Management Systems', 4, 'March 2020', 'Theory(only)', 1),
(58, 'Bachelor of Engineering (Hons)', 'ECB 2091', 'Database Management Systems Lab', 4, 'March 2020', 'Theory with Lab', 1),
(59, 'Bachelor of Engineering (Hons)', 'EEB 3243', 'Measurements and Instrumentation', 4, 'March 2020', 'Theory(only)', 1),
(60, 'Bachelor of Engineering (Hons)', 'EEB 2263', 'Control System', 4, 'March 2020', 'Theory(only)', 1),
(61, 'Bachelor of Engineering (Hons)', 'ECB 2023', 'Computer Organisation and Architecture', 4, 'March 2020', 'Theory(only)', 1),

(62, 'Bachelor of Engineering (Hons)', 'ECB 3123', 'Artificial Intelligence', 5, 'AUGUST & OCT 2019', 'Theory(only)', 1),
(63, 'Bachelor of Engineering (Hons)', 'ECB 2113', 'Software Engineering', 5, 'AUGUST & OCT 2019', 'Theory(only)', 1),
(64, 'Bachelor of Engineering (Hons)', 'EEB 3153', 'Communication Theory', 5, 'AUGUST & OCT 2019', 'Theory(only)', 1),
(65, 'Bachelor of Engineering (Hons)', 'EEB 2213', 'Electromagnetic Wave', 5, 'AUGUST & OCT 2019', 'Theory(only)', 1),
(66, 'Bachelor of Engineering (Hons)', 'EEB 3163', 'Integrated Circuit Design', 5, 'AUGUST & OCT 2019', 'Theory(only)', 1),
(67, 'Bachelor of Engineering (Hons)', 'UOB 3362', 'Introduction to Organizational Behavior', 5, 'AUGUST & OCT 2019', 'Theory(only)', 1),

(69, 'Bachelor of Engineering (Hons)', 'ECB 3173', 'Computer Networks', 6, 'MARCH 2019', 'Theory(only)', 1),
(70, 'Bachelor of Engineering (Hons)', 'ECB 3171', 'Computer Networks Lab', 6, 'MARCH 2019', 'Theory with Lab', 1),
(71, 'Bachelor of Engineering (Hons)', 'ECB 4233', 'Serverside Programming', 6, 'MARCH 2019', 'Theory(only)', 1),
(72, 'Bachelor of Engineering (Hons)', 'ECB 4241', 'Serverside Programming Lab', 6, 'MARCH 2019', 'Theory with Lab', 1),
(73, 'Bachelor of Engineering (Hons)', 'EKB 3192', 'Project Management', 6, 'MARCH 2019', 'Theory(only)', 1),
(74, 'Bachelor of Engineering (Hons)', 'EYB 4502', 'Engineer & Society', 6, 'MARCH 2019', 'Theory(only)', 1),
(75, 'Bachelor of Engineering (Hons)', 'ECB 3251', 'Seminar & Workshop / special tools', 6, 'MARCH 2019', 'Theory(only)', 1),

(76, 'Bachelor of Engineering (Hons)', 'EEB 3123', 'Digital Signal Processing', 7, 'July & Oct 2018', 'Theory(only)', 1),
(77, 'Bachelor of Engineering (Hons)', 'EEB 3111', 'Digital Signal Processing Lab', 7, 'July & Oct 2018', 'Theory with Lab', 1),
(78, 'Bachelor of Engineering (Hons)', 'ECB 3203', 'Embedded System', 7, 'July & Oct 2018', 'Theory(only)', 1),
(79, 'Bachelor of Engineering (Hons)', 'ECB 3211', 'Embedded System Lab', 7, 'July & Oct 2018', 'Theory with Lab', 1),
(80, 'Bachelor of Engineering (Hons)', 'ECB 4343', 'Engineering Elective 1(Data Warehousing and Data Mining)', 7, 'July & Oct 2018', 'Theory(only)', 1),
(81, 'Bachelor of Engineering (Hons)', 'ECB 4252', 'Final Year Project 1', 7, 'July & Oct 2018', 'Theory(only)', 1),

(82, 'Bachelor of Engineering (Hons)', 'ECB 4263', 'Mobile Computing', 8, 'March 2018', 'Theory(only)', 1),
(83, 'Bachelor of Engineering (Hons)', 'ECB 4271', 'Mobile Computing Lab', 8, 'March 2018', 'Theory with Lab', 1),
(84, 'Bachelor of Engineering (Hons)', 'ECB 4254', 'Final Year Project (FYP 2)', 8, 'March 2018', 'Theory(only)', 1),
(85, 'Bachelor of Engineering (Hons)', 'ECB 4493', 'Engineering Elective 2 [Crypto graphy and network security]', 8, 'March 2018', 'Theory(only)', 1),
(86, 'Bachelor of Engineering (Hons)', 'ECB 4313', 'Data Analytics', 8, 'March 2018', 'Theory(only)', 1),
(87, 'Bachelor of Engineering (Hons)', 'ECB 4353', 'Elective 3(Big Data)', 8, 'March 2018', 'Theory(only)', 1),

(88, 'Bachelor of Engineering (Hons)', 'EYB 1013', 'Engineering Mathematics 1', 1, 'OCT 21 & AUG 21', 'Theory(only)', 2),
(89, 'Bachelor of Engineering (Hons)', 'EYB 1023', 'Computer Programming', 1, 'OCT 21 & AUG 21', 'Theory(only)', 2),
(90, 'Bachelor of Engineering (Hons)', 'EYB 1031', 'Computer Programming Lab', 1, 'OCT 21 & AUG 21', 'Theory with Lab', 2),
(91, 'Bachelor of Engineering (Hons)', 'EYB 1043', 'Engineering Graphics', 1, 'OCT 21 & AUG 21', 'Theory(only)', 2),
(92, 'Bachelor of Engineering (Hons)', 'USB 1062', 'Self-Development & Leadership Building', 1, 'OCT 21 & AUG 21', 'Theory(only)', 2),
(93, 'Bachelor of Engineering (Hons)', 'UTB 1053', 'English for Professional Communication', 1, 'OCT 21 & AUG 21', 'Theory(only)', 2),
(94, 'Bachelor of Engineering (Hons)', 'MPU 3163', 'Bahasa Melayu Komunikasi 2', 1, 'OCT 21 & AUG 21', 'Theory(only)', 2),
(95, 'Bachelor of Engineering (Hons)', 'MPU 3113', 'Hubungan Etnik', 1, 'OCT 21 & AUG 21', 'Theory(only)', 2),

(96, 'Bachelor of Engineering (Hons)', 'EYB 1083', 'Engineering Mathematics 2', 2, 'March 21', 'Theory(only)', 2),
(97, 'Bachelor of Engineering (Hons)', 'MPU 3163', 'Bahasa Melayu Komunikasi 2', 2, 'March 21', 'Theory(only)', 2),
(98, 'Bachelor of Engineering (Hons)', 'MPU 3113', 'Hubungan Etnik', 2, 'March 21', 'Theory(only)', 2),
(99, 'Bachelor of Engineering (Hons)', 'EVB 1022', 'Statics & Dynamics', 2, 'March 21', 'Theory(only)', 2),
(100, 'Bachelor of Engineering (Hons)', 'EVB 1093', 'Surveying', 2, 'March 21', 'Theory(only)', 2),
(101, 'Bachelor of Engineering (Hons)', 'EVB 1101', 'Surveying Practices', 2, 'March 21', 'Theory(only)', 2),
(102, 'Bachelor of Engineering (Hons)', 'EVB 1112', 'Building Design & Drawing (AUTOCADD)', 2, 'March 21', 'Theory(only)', 2),
(103, 'Bachelor of Engineering (Hons)', 'EVB 2253', 'Fluid Mechanics', 2, 'March 21', 'Theory(only)', 2),
(104, 'Bachelor of Engineering (Hons)', 'EVB 2163', 'Civil Engineering Materials', 2, 'March 21', 'Theory(only)', 2),

(105, 'Bachelor of Engineering (Hons)', 'EYB 2153', 'Engineering Mathematics 3', 3, 'Aug20 & Oct20', 'Theory(only)', 2),
(106, 'Bachelor of Engineering (Hons)', 'EVB 2173', 'Structural analysis 1', 3, 'Aug20 & Oct20', 'Theory(only)', 2),
(107, 'Bachelor of Engineering (Hons)', 'EVB 2183', 'Transportation Engineering', 3, 'Aug20 & Oct20', 'Theory(only)', 2),
(108, 'Bachelor of Engineering (Hons)', 'EVB 2262', 'Civil Engineering Lab 1', 3, 'Aug20 & Oct20', 'Theory with Lab', 2),
(109, 'Bachelor of Engineering (Hons)', 'EVB 2193', 'Hydraulics', 3, 'Aug20 & Oct20', 'Theory(only)', 2),
(110, 'Bachelor of Engineering (Hons)', 'UEB 2542', 'Entreprenuership', 3, 'Aug20 & Oct20', 'Theory(only)', 2),

(111, 'Bachelor of Engineering (Hons)', 'EYB 3352', 'R&D Methodology', 4, 'Mar20 & Apr20', 'Theory(only)', 2),
(112, 'Bachelor of Engineering (Hons)', 'EVB 2233', 'Structural Analysis 2', 4, 'Mar20 & Apr20', 'Theory(only)', 2),
(113, 'Bachelor of Engineering (Hons)', 'EVB 2273', 'Hydrology', 4, 'Mar20 & Apr20', 'Theory(only)', 2),
(114, 'Bachelor of Engineering (Hons)', 'EVB 3323', 'Environmental Engineering', 4, 'Mar20 & Apr20', 'Theory(only)', 2),
(115, 'Bachelor of Engineering (Hons)', 'EVB 3303', 'Traffic Engineering', 4, 'Mar20 & Apr20', 'Theory(only)', 2),
(116, 'Bachelor of Engineering (Hons)', 'EVB 3343', 'Soil Mechanics', 4, 'Mar20 & Apr20', 'Theory(only)', 2),

(117, 'Bachelor of Engineering (Hons)', 'UOB 3362', 'Introduction to Organizational Behaviour', 5, 'Aug19 & Oct19', 'Theory(only)', 2),
(118, 'Bachelor of Engineering (Hons)', 'EVB 3332', 'Civil Engineering Lab 2', 5, 'Aug19 & Oct19', 'Theory with Lab', 2),
(119, 'Bachelor of Engineering (Hons)', 'EVB 3313', 'Structural Design 1', 5, 'Aug19 & Oct19', 'Theory(only)', 2),
(120, 'Bachelor of Engineering (Hons)', 'EVB 4533', 'Geotechnical Engineering', 5, 'Aug19 & Oct19', 'Theory(only)', 2),
(121, 'Bachelor of Engineering (Hons)', 'EVB 4603', 'Highway Engineering', 5, 'Aug19 & Oct19', 'Theory(only)', 2),

(122, 'Bachelor of Engineering (Hons)', 'EYB 3633', 'Operational Research', 6, 'March 19', 'Theory(only)', 2),
(123, 'Bachelor of Engineering (Hons)', 'EYB 4502', 'Engineering & Society', 6, 'March 19', 'Theory(only)', 2),
(124, 'Bachelor of Engineering (Hons)', 'EVB 3393', 'Structural design & drawing', 6, 'March 19', 'Theory(only)', 2),
(125, 'Bachelor of Engineering (Hons)', 'EVB 3383', 'Structural Design 2', 6, 'March 19', 'Theory(only)', 2),
(126, 'Bachelor of Engineering (Hons)', 'EVB 4443', 'Construction Management & Planning', 6, 'March 19', 'Theory(only)', 2),
(127, 'Bachelor of Engineering (Hons)', 'EVB 4531', 'Technical Seminar', 6, 'March 19', 'Theory(only)', 2),

(128, 'Bachelor of Engineering (Hons)', 'EYB 4663', 'Database Management', 7, 'Jul18 & Oct18', 'Theory(only)', 2),
(129, 'Bachelor of Engineering (Hons)', 'EVB 4492', 'Final Year Project 1', 7, 'Jul18 & Oct18', 'Theory(only)', 2),
(130, 'Bachelor of Engineering (Hons)', 'EVB 3374', 'Intergrated Design Project', 7, 'Jul18 & Oct18', 'Theory(only)', 2),
(131, 'Bachelor of Engineering (Hons)', 'EVB 4453', 'Structural Design 3', 7, 'Jul18 & Oct18', 'Theory(only)', 2),
(132, 'Bachelor of Engineering (Hons)', 'EVB 4463', 'Advance Foundation Design', 7, 'Jul18 & Oct18', 'Theory(only)', 2),
(133, 'Bachelor of Engineering (Hons)', 'EYB 4653', 'Numerical Method for Engineering Analysis', 7, 'Jul18 & Oct18', 'Theory(only)', 2),

(134, 'Bachelor of Engineering (Hons)', 'EVB 4554', 'Final Year Project 2', 8, 'Mar-18', 'Theory(only)', 2),
(135, 'Bachelor of Engineering (Hons)', 'EVB 3563', 'Finite Element Method fo SA', 8, 'Mar-18', 'Theory(only)', 2),
(136, 'Bachelor of Engineering (Hons)', 'EVB 4573', 'Advance Structural Design', 8, 'Mar-18', 'Theory(only)', 2),
(137, 'Bachelor of Engineering (Hons)', 'EVB 4623', 'Environment Impact Assessment', 8, 'Mar-18', 'Theory(only)', 2);










