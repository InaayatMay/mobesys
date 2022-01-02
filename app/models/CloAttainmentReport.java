package models;

import io.ebean.annotation.Sql;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Sql
public class CloAttainmentReport {
    @Column(name = "student_id")
    public Long studentId;

    @Column(name = "code_number")
    public String codeNumber;

    @Column(name = "student_name")
    public String studentName;

    @Column(name = "clo_code")
    public String cloCode;

    @Column(name = "plo_code")
    public String ploCode;

    @Column(name = "total_weightage")
    public Double totalWeightage;

    @Column(name = "total_clo_weightage")
    public Double totalCloWeightage;

    @Column(name = "clo_attainment")
    public Double cloAttainment;
}
