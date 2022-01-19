package services;

import io.ebean.Ebean;
import models.Lecturer;
import models.LecturerCurrentSubject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class LecturerService {
    private final Logger logger = LoggerFactory.getLogger("application");

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
            if(!lecturerCurrentSubject.isCompleted || lecturerCurrentSubject.currentPage.equals("Student")) {
                String sql = "update lecturer_current_subject\n" +
                        "set is_completed = :isCompleted, current_page = :currentPage\n" +
                        "where id = :id;";
                int updatedRow = Ebean.createSqlUpdate(sql)
                        .setParameter("isCompleted", isCompleted)
                        .setParameter("currentPage", currentPage)
                        .setParameter("id", lecturerCurrentSubject.id)
                        .execute();
            }
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

    public List<Lecturer> getOtherCourseLecturerList(Long lecturerId) {
        String sql = "select distinct lecturer.*\n" +
                "from lecturer_course_map\n" +
                "left join lecturer on lecturer_id = lecturer.id\n" +
                "where lecturer.id <> :lecturerId;";

        return Ebean.findNative(Lecturer.class, sql)
                .setParameter("lecturerId", lecturerId)
                .findList();
    }

    public void updateLecturer(Lecturer lecturer) {
        String sql = "update lecturer\n" +
                "set first_name=:first_name , last_name=:last_name , gender=:gender , code_number=:code_number , " +
                "email=:email , phone_number=:phone_number, birth_month =:birth_month, birth_day=:birth_day , birth_year=:birth_year\n" +
                "where id = 1;";
        int updatedRow = Ebean.createSqlUpdate(sql)
                .setParameter("first_name", lecturer.firstName)
                .setParameter("last_name", lecturer.lastName)
                .setParameter("gender", lecturer.gender)
                .setParameter("code_number", lecturer.codeNumber)
                .setParameter("email", lecturer.email)
                .setParameter("phone_number", lecturer.phoneNumber)
                .setParameter("birth_month",lecturer.birthMonth )
                .setParameter("birth_day", lecturer.birthDay)
                .setParameter("birth_year", lecturer.birthYear)
                .execute();
        Ebean.update(lecturer);
    }
}
