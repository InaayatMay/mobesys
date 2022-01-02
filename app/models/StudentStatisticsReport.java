package models;

import io.ebean.annotation.Sql;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Sql
public class StudentStatisticsReport {
    @Column(name = "student_id")
    public Long studentId;

    @Column(name = "code_number")
    public String codeNumber;

    @Column(name = "student_name")
    public String studentName;

    @Column(name = "assessment_type")
    public String assessmentType;

    @Column(name = "total_statistics_marks")
    public Double totalStatisticsMarks;

    @Column(name = "number_of_attempt")
    public Integer numberOfAttempt;
}
