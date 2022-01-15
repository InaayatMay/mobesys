package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name="request")
public class Request {

    @Id
    public Long id;

    @Column(name = "from_lecturer_id")
    public Long fromLecturerId;

    @Column(name = "to_lecturer_id")
    public Long toLecturerId;

    @Column(name = "course_information_id")
    public Long courseInformationId;

    public String request;

    @Column(name = "is_approved")
    public Boolean isApproved;

    @Column(name = "created_at")
    public Timestamp createdAt;

    @Column(name = "approved_at")
    public Timestamp approvedAt;
}
