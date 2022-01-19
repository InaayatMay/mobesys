package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="lecturer")
public class Lecturer {

    @Id
    public Long id;

    @Column(name = "first_name")
    public String firstName;

    @Column(name = "last_name")
    public String lastName;

    public String gender;

    @Column(name = "code_number")
    public String codeNumber;

    public String email;

    public String password;

    public String image;

    @Column(name = "phone_number")
    public String phoneNumber;

    @Column(name = "birth_day")
    public int birthDay;

    @Column(name = "birth_month")
    public int birthMonth;

    @Column(name = "birth_year")
    public int birthYear;
}
