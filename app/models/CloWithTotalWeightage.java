package models;

import io.ebean.annotation.Sql;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Sql
public class CloWithTotalWeightage {

    @Column(name = "clo_title")
    public String cloTitle;

    @Column(name = "plo_code")
    public String ploCode;

    @Column(name = "total_weightage")
    public int totalWeightage;

    @Column(name = "total_full_marks")
    public int totalFullMarks;
}
