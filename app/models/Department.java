package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="department")
public class Department {

    @Id
    public Long id;

    @Column(name = "department_name")
    public String departmentName;

    @Column(name = "school_id")
    public Long schoolId;
}
