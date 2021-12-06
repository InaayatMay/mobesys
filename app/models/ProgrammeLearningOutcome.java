package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "programme_learning_outcome")
public class ProgrammeLearningOutcome {

    @Id
    public Long id;

    public String title;

    public String code;

    @Column(name = "course_information_id")
    public Long courseInformationId;

    @Column(name = "lecturer_id")
    public Long lecturerId;
}
