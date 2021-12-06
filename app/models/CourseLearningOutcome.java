package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "course_learning_outcome")
public class CourseLearningOutcome {

    @Id
    public Long id;

    public String title;

    @Column(name = "plo_code")
    public String ploCode;

    @Column(name = "course_information_id")
    public Long courseInformationId;

    @Column(name = "lecturer_id")
    public Long lecturerId;
}
