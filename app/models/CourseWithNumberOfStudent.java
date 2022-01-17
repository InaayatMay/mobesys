package models;

import io.ebean.annotation.Sql;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Sql
public class CourseWithNumberOfStudent {

    @Column(name = "course_information_id")
    public Long courseId;

    @Column(name = "course_code")
    public String courseCode;

    @Column(name = "course_name")
    public String courseName;

    @Column(name = "programme")
    public String programme;

    @Column(name = "number_of_student")
    public int numberOfStudents;
}
