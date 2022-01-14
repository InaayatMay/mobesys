package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "student_course_map")
public class StudentToCourseMap {

    @Id
    public Long id;

    @Column(name = "student_id")
    public Long studentId;

    @Column(name = "course_information_id")
    public Long courseInformationId;
}
