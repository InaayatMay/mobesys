package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="lecturer_course_map")
public class LecturerCourseMap {

    @Id
    public Long id;

    @Column(name = "lecturer_id")
    public Long lecturerId;

    @Column(name = "course_information_id")
    public Long courseInformationId;

    @Column(name = "department_id")
    public Long departmentId;

    @Column(name = "school_id")
    public Long schoolId;
}
