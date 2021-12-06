package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "course_information")
public class CourseInformation {

    @Id
    public Long id;

    public String programme;

    @Column(name = "course_code")
    public String courseCode;

    @Column(name = "course_name")
    public String courseName;

    public String semester;

    @Column(name = "intake_batch")
    public String intakeBatch;

    @Column(name = "lecturer_id")
    public Long lecturerId;

    @Column(name = "course_type")
    public String courseType;
}