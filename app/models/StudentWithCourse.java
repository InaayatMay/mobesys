package models;

import io.ebean.annotation.Sql;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Sql
public class StudentWithCourse {

    @Column(name = "student_id")
    public Long studentId;

    @Column(name = "course_code")
    public String courseCode;

    @Column(name = "course_name")
    public String courseName;
}
