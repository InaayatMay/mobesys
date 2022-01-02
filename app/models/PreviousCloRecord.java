package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "previous_clo_record")
public class PreviousCloRecord {

    @Id
    public Long id;

    @Column(name = "clo_code")
    public String cloCode;

    @Column(name = "previous_semester_class_average")
    public Double previousSemesterClassAverage;

    public String comments;

    @Column(name = "lecturer_id")
    public Long lecturerId;

    @Column(name = "course_information_id")
    public Long courseInformationId;
}