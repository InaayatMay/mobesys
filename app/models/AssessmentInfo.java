package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "assessment_info")
public class AssessmentInfo {

    @Id
    public Long id;

    public String assessment;

    @Column(name = "assessment_type")
    public String assessmentType;

    @Column(name = "full_marks")
    public int fullMarks;

    @Column(name = "weightage")
    public int weightage;

    @Column(name = "clo_title")
    public String cloTitle;

    @Column(name = "lecturer_id")
    public Long lecturerId;

    @Column(name = "course_information_id")
    public Long courseInformationId;
}