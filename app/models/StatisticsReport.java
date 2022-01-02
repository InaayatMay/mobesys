package models;

import io.ebean.annotation.Sql;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Sql
public class StatisticsReport {

    @Column(name = "assessment_type")
    public String assessmentType;

    @Column(name = "total_weightage")
    public Double totalWeightage;

    @Column(name = "total_statistics_marks")
    public Double totalStatisticsMarks;
}
