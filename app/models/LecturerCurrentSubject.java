package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="lecturer_current_subject")
public class LecturerCurrentSubject {

    @Id
    public Long id;

    @Column(name = "lecturer_id")
    public Long lecturerId;

    @Column(name = "course_information_id")
    public Long courseInformationId;

    @Column(name = "course_name")
    public String courseName;

    @Column(name = "is_completed")
    public Boolean isCompleted;

    @Column(name = "current_page")
    public String currentPage;
}
