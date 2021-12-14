package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="school")
public class School {

    @Id
    public Long id;

    @Column(name = "school_name")
    public String schoolName;
}
