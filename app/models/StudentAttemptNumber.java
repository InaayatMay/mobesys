package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "student_number_of_attempt")
public class StudentAttemptNumber {

    @Id
    public Long id;

    @Column(name = "student_id")
    public Long studentId;

    @Column(name = "number_of_attempt")
    public int numberOfAttempt;

    @Column(name = "lecturer_id")
    public Long lecturerId;

    @Column(name = "course_information_id")
    public Long courseInformationId;
}
