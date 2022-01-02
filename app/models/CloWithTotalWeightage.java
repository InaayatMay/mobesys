package models;

import io.ebean.annotation.Sql;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Sql
public class CloWithTotalWeightage {

    @Column(name = "clo_code")
    public String cloCode;

    @Column(name = "plo_code")
    public String ploCode;

    @Column(name = "total_weightage")
    public Double totalWeightage;

    @Column(name = "total_full_marks")
    public int totalFullMarks;
}
