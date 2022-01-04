package services;

import io.ebean.Ebean;
import models.Lecturer;
import models.LecturerCurrentSubject;

import java.util.List;
import java.util.Optional;

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

    public Lecturer getLecturerById (Long id) {
        return Ebean.find(Lecturer.class).where().eq("id", id).findOne();
    }

    public void updatePassword(Lecturer lecturer, String newPassword) {
        lecturer.password = newPassword;
        Ebean.save(lecturer);
    }

    public Optional<LecturerCurrentSubject> getLecturerCurrentSubjectState(Long lecturerId, Long courseId) {
        return Ebean.find(LecturerCurrentSubject.class).where().and()
                .eq("lecturerId", lecturerId)
                .eq("courseInformationId", courseId)
                .endAnd().findOneOrEmpty();
    }

    public List<LecturerCurrentSubject> getLecturerSubjectsStateList(Long lecturerId) {
        return Ebean.find(LecturerCurrentSubject.class).where().eq("lecturerId", lecturerId).findList();
    }

    public void saveLecturerCurrentSubjectState(Long lecturerId, Long courseId, String courseName, Boolean isCompleted, String currentPage) {
        Optional<LecturerCurrentSubject> optionalLecturerCurrentSubject = getLecturerCurrentSubjectState(lecturerId, courseId);
        if(optionalLecturerCurrentSubject.isPresent()) {
            LecturerCurrentSubject lecturerCurrentSubject = optionalLecturerCurrentSubject.get();
            lecturerCurrentSubject.isCompleted = isCompleted;
            lecturerCurrentSubject.currentPage = currentPage;
            Ebean.update(lecturerCurrentSubject);
        }
        else {
            LecturerCurrentSubject lecturerCurrentSubject = new LecturerCurrentSubject();
            lecturerCurrentSubject.lecturerId = lecturerId;
            lecturerCurrentSubject.courseInformationId = courseId;
            lecturerCurrentSubject.courseName = courseName;
            lecturerCurrentSubject.isCompleted = isCompleted;
            lecturerCurrentSubject.currentPage = currentPage;
            Ebean.save(lecturerCurrentSubject);
        }
    }

}
