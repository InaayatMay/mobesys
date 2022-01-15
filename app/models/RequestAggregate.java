package models;

import io.ebean.annotation.Sql;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.sql.Timestamp;

@Entity
@Sql
public class RequestAggregate {

    public Long id;

    @Column(name = "from_lecturer_id")
    public Long requestById;

    @Column(name = "request_by")
    public String requestByName;

    @Column(name = "to_lecturer_id")
    public Long requestToId;

    @Column(name = "request_to")
    public String requestToName;

    @Column(name = "course_information_id")
    public Long courseInformationId;

    @Column(name = "course_code")
    public String courseCode;

    @Column(name = "course_name")
    public String courseName;

    public String request;

    @Column(name = "is_approved")
    public boolean isApproved;

    @Column(name = "created_at")
    public Timestamp createdAt;

    @Column(name = "approved_at")
    public Timestamp approvedAt;
}
