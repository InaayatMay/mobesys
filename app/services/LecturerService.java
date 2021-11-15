package services;

import io.ebean.Ebean;
import models.Lecturer;


public class LecturerService {
    public boolean authenticate(String email, String password) {
        Lecturer lecturer = Ebean.find(Lecturer.class).where().and().eq("email",email)
                .eq("password", password)
                .endAnd().findOne();
        return lecturer != null;
    }

    public Lecturer getLecturerByEmail (String email) {
        return Ebean.find(Lecturer.class).where().eq("email", email).findOne();
    }

    public void updatePassword(Lecturer lecturer, String newPassword) {
        lecturer.password = newPassword;
        Ebean.save(lecturer);
    }

}
