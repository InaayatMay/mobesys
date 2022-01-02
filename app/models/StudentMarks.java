package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "student_marks")
public class StudentMarks {

    @Id
    public Long id;

    @Column(name = "student_id")
    public Long studentId;

    @Column(name = "assessment_id")
    public Long assessmentId;

    public Double marks;

    @Column(name = "lecturer_id")
    public Long lecturerId;

    @Column(name = "course_information_id")
    public Long courseInformationId;
}
