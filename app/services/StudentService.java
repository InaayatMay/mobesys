package services;

import io.ebean.Ebean;
import io.ebean.annotation.Transactional;
import models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class StudentService {
    private final Logger logger = LoggerFactory.getLogger("application");

    public List<Student> getStudentList(Long lecturerId) {
        return Ebean.find(Student.class).where().eq("lecturer_id", lecturerId)
                .orderBy().desc("id").findList();
    }

    public List<Student> getStudentListByLecturerAndCourse(Long lecturerId, Long courseInformationId, int limit) {
        String sql = "SELECT student.*\n" +
                "FROM student_course_map\n" +
                "left join student on student.id = student_course_map.student_id\n" +
                "where course_information_id = :courseInformationId and student.lecturer_id = :lecturerId\n" +
                "order by student_course_map.id desc\n" +
                "limit :limit;";

        return Ebean.findNative(Student.class, sql)
                .setParameter("courseInformationId", courseInformationId)
                .setParameter("lecturerId", lecturerId)
                .setParameter("limit", limit)
                .findList();
    }

    public Long saveStudent(String codeNumber, String firstName, String lastName, String gender, String currentSemester,
                            String email, Long lecturerId) {

        Student student = new Student();
        student.codeNumber = codeNumber;
        student.firstName = firstName;
        student.lastName = lastName;
        student.gender = gender;
        student.currentSemester = currentSemester;
        student.email = email;
        student.lecturerId = lecturerId;

        Ebean.save(student);
        return student.id;
    }

    @Transactional
    public void saveStudentToCourseInformationMap(Long lecturerId, Long studentId, CourseInformation courseInformation,
                                               List<AssessmentInfo> assessmentInfoList) {

        Student student = getStudent(studentId);
        student.program = courseInformation.programme;
        Ebean.save(student);

        StudentToCourseMap map = new StudentToCourseMap();
        map.courseInformationId = courseInformation.id;
        map.studentId = studentId;
        Ebean.save(map);

        saveStudentMarks(studentId, lecturerId, courseInformation.id, assessmentInfoList);
    }

    public void saveStudentMarks(Long studentId, Long lecturerId, Long courseInformationId, List<AssessmentInfo> assessmentInfoList) {
        for(AssessmentInfo assessmentInfo: assessmentInfoList) {
            StudentMarks marks = new StudentMarks();
            marks.studentId = studentId;
            marks.lecturerId = lecturerId;
            marks.courseInformationId = courseInformationId;
            marks.assessmentId = assessmentInfo.id;
            marks.marks = 0.0;

            Ebean.save(marks);
        }
    }

    public int hasDuplicateCodeNumber(String codeNumber, Long lecturerId) {
        int count = Ebean.find(Student.class).where()
                .and()
                .eq("lecturer_id", lecturerId)
                .eq("code_number", codeNumber)
                .endAnd()
                .findCount();
        return count;
    }

    public int hasDuplicateName(String firstName, String lastName, Long lecturerId) {
        int count = Ebean.find(Student.class).where()
                .and()
                .eq("lecturer_id", lecturerId)
                .eq("first_name", firstName)
                .eq("last_name", lastName)
                .endAnd()
                .findCount();
        return count;
    }

    public int hasDuplicateEmail(String email, Long lecturerId) {
        int count = Ebean.find(Student.class).where()
                .and()
                .eq("lecturer_id", lecturerId)
                .eq("email", email)
                .endAnd()
                .findCount();
        return count;
    }

    public void deleteStudent(Long studentId) {
        String sqlMarks = "delete from student_marks where student_id = :studentId;";
        int deleteMarks = Ebean.createSqlUpdate(sqlMarks).setParameter("studentId", studentId).execute();

        String sqlNumberOfAttempt = "delete from student_number_of_attempt where student_id = :studentId;";
        int deleteNumberOfAttempt = Ebean.createSqlUpdate(sqlNumberOfAttempt).setParameter("studentId", studentId).execute();

        String sqlCourse = "delete from student_course_map where student_id = :studentId;";
        int deleteCourse = Ebean.createSqlUpdate(sqlCourse).setParameter("studentId", studentId).execute();

        int deleteStudent = Ebean.delete(Student.class, studentId);

        logger.debug("Deleted : " + deleteStudent);
    }

    public void deleteStudentCourseMap(Long studentId, Long courseInformationId) {
        String sqlMarks = "delete from student_marks where student_id = :studentId and course_information_id = :courseId;";
        int deleteMarks = Ebean.createSqlUpdate(sqlMarks).setParameter("studentId", studentId)
                .setParameter("courseId", courseInformationId).execute();

        String sql = "delete from student_course_map where student_id = :studentId and course_information_id = :courseId";
        int deleteMap = Ebean.createSqlUpdate(sql).setParameter("studentId", studentId)
                .setParameter("courseId", courseInformationId).execute();
    }

    public void updateStudent(Student student) {
        Ebean.update(student);
    }

    public Student getStudent(Long studentId) {
        return Ebean.find(Student.class, studentId);
    }

    public List<StudentMarks> getStudentMarksList(Long studentId, Long lecturerId, Long courseId) {
        return Ebean.find(StudentMarks.class).where().and()
                .eq("student_id", studentId)
                .eq("lecturer_id", lecturerId)
                .eq("course_information_id", courseId)
                .endAnd()
                .orderBy()
                .asc("id")
                .findList();
    }

    public StudentMarks getStudentMarks(Long studentMarksId) {
        return Ebean.find(StudentMarks.class, studentMarksId);
    }

    public void updateStudentMarks(Long studentMarksId, Double marks) {
        StudentMarks studentMarks = getStudentMarks(studentMarksId);
        if(marks >= 0.0) {
            studentMarks.marks = marks;
            Ebean.update(studentMarks);
        }
    }

    public List<StatisticsReport> getStatisticsMarks(Long lecturerId, Long courseInformationId) {
        String sql = "SELECT ai.assessment_type, round(sum(round((sm.marks/ai.full_marks)*ai.weightage, 2)), 2) as total_statistics_marks,\n" +
                "round(sum(ai.weightage), 2) as total_weightage\n" +
                "FROM student_marks as sm\n" +
                "left join assessment_info as ai on assessment_id = ai.id\n" +
                "where sm.lecturer_id = :lecturerId and sm.course_information_id = :courseInformationId and ai.assessment_type is not null\n" +
                "group by ai.assessment_type;";

        return Ebean.findDto(StatisticsReport.class, sql)
                .setParameter("lecturerId", lecturerId)
                .setParameter("courseInformationId", courseInformationId)
                .findList();
    }

    public List<StudentStatisticsReport> getStudentStatisticsReport(Long lecturerId, Long courseInformationId) {
        String sql = "select sm.student_id, std.code_number, concat(std.first_name, ' ', std.last_name) as student_name, attempt.number_of_attempt, ai.assessment_type, round(sum(round((sm.marks/ai.full_marks)*ai.weightage, 2)), 2) as total_statistics_marks\n" +
                "FROM student_marks as sm\n" +
                "left join assessment_info as ai on sm.assessment_id = ai.id\n" +
                "left join student as std on std.id = sm.student_id\n" +
                "left join student_number_of_attempt as attempt on attempt.student_id = sm.student_id\n" +
                "where sm.lecturer_id = :lecturerId and sm.course_information_id = :courseInformationId and std.code_number is not null\n" +
                "group by ai.assessment_type, sm.student_id;";

        return Ebean.findDto(StudentStatisticsReport.class, sql)
                .setParameter("lecturerId", lecturerId)
                .setParameter("courseInformationId", courseInformationId)
                .findList();
    }

    public StudentAttemptNumber getStudentAttemptNumber(Long studentAttemptNumberId) {
        return Ebean.find(StudentAttemptNumber.class, studentAttemptNumberId);
    }

    public Optional<StudentAttemptNumber> getStudentAttemptNumberByStudent(Long lecturerId, Long courseId, Long studentId) {
        Optional<StudentAttemptNumber> optional = Ebean.find(StudentAttemptNumber.class).where()
                .and()
                .eq("lecturerId", lecturerId)
                .eq("courseId", courseId)
                .eq("studentId", studentId)
                .endAnd()
                .findOneOrEmpty();

        return optional;
    }

    public void saveStudentAttemptNumber(Long lecturerId, Long courseId, Long studentId, int numberOfAttempt) {
        Optional<StudentAttemptNumber> optional = getStudentAttemptNumberByStudent(lecturerId , courseId, studentId);
        if(optional.isPresent()) {
            StudentAttemptNumber studentAttemptNumber = optional.get();
            studentAttemptNumber.numberOfAttempt = numberOfAttempt;
            Ebean.update(numberOfAttempt);
        }
        else {
            StudentAttemptNumber studentAttemptNumber = new StudentAttemptNumber();
            studentAttemptNumber.courseInformationId = courseId;
            studentAttemptNumber.lecturerId = lecturerId;
            studentAttemptNumber.studentId = studentId;
            studentAttemptNumber.numberOfAttempt = numberOfAttempt;

            Ebean.save(studentAttemptNumber);
        }
    }

    public List<CloAttainmentReport> getCloAttainments(Long lecturerId, Long courseInformationId) {
        String sql = "select sm.student_id, std.code_number, concat(std.first_name, ' ', std.last_name) as student_name, ai.clo_code, clo.plo_code,\n" +
                "round(sum(round((sm.marks/ai.full_marks)*ai.weightage, 2))/2, 2) as total_weightage, sum(ai.weightage)/2 as total_clo_weightage,\n" +
                "round((round(sum(round((sm.marks/ai.full_marks)*ai.weightage, 2)), 2)/sum(ai.weightage))*100, 2) as clo_attainment\n" +
                "FROM student_marks as sm\n" +
                "left join assessment_info as ai on sm.assessment_id = ai.id\n" +
                "left join student as std on std.id = sm.student_id\n" +
                "left join course_learning_outcome as clo on clo.code = ai.clo_code\n" +
                "where sm.lecturer_id = :lecturerId and sm.course_information_id = :courseInformationId\n" +
                "group by ai.clo_code, sm.student_id;";

        return Ebean.findDto(CloAttainmentReport.class, sql)
                .setParameter("lecturerId", lecturerId)
                .setParameter("courseInformationId", courseInformationId)
                .findList();
    }

    public List<StudentToCourseMap> getStudentToCourseMapList(Long courseId) {
        return Ebean.find(StudentToCourseMap.class).where().eq("course_information_id", courseId).findList();
    }

    public List<Student> getUnmappedStudentList(Long lecturerId, Long courseId) {
        List<Student> studentList = getStudentList(lecturerId);
        List<StudentToCourseMap> studentToCourseMapList = getStudentToCourseMapList(courseId);
        List<Long> mappedStudentIdList = studentToCourseMapList.stream().map(std -> std.studentId).collect(Collectors.toList());

        List<Student> unmappedList = new ArrayList<>();
        for(int i=0; i<studentList.size(); i++) {
            if(!mappedStudentIdList.contains(studentList.get(i).id)) {
                unmappedList.add(studentList.get(i));
            }
        }

        return unmappedList;
    }

    /*public Map<Long, Integer> getStudentNumberOfCourse(Long lecturerId) {

    }*/
}
