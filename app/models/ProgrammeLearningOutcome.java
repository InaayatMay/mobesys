package models;

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
}
