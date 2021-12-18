package services;

import io.ebean.Ebean;
import models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class CourseInformationService {
    private final Logger logger = LoggerFactory.getLogger("application");

    public LecturerCourseMap saveLecturerCourseMap(Long lecturerId, Long courseId, Long departmentId, Long schoolId) {
        LecturerCourseMap lecturerCourseMap = new LecturerCourseMap();
        lecturerCourseMap.lecturerId = lecturerId;
        lecturerCourseMap.courseInformationId = courseId;
        lecturerCourseMap.departmentId = departmentId;
        lecturerCourseMap.schoolId = schoolId;

        Ebean.save(lecturerCourseMap);

        return lecturerCourseMap;
    }

    public List<School> getSchoolList() {
        return Ebean.find(School.class).findList();
    }

    public List<Department> getDepartmentListBySchool(Long schoolId) {
        return Ebean.find(Department.class).where().eq("school_id", schoolId).findList();
    }

    public List<CourseInformation> getCourseInformationByDept(Long departmentId) {
        return Ebean.find(CourseInformation.class).where().eq("department_id", departmentId).findList();
    }

    public CourseInformation getCourseInformationById(Long courseInformationId) {
        return Ebean.find(CourseInformation.class).where().eq("id", courseInformationId).findOne();
    }

    public List<ProgrammeLearningOutcome> getProgrammeLearningOutcomeList() {
        return Ebean.find(ProgrammeLearningOutcome.class).findList();
    }

    public List<ProgrammeLearningOutcome> getUnlinkedPloList(Long courseInformationId) {
        String sql = "SELECT plo.*\n" +
                "FROM programme_learning_outcome AS plo\n" +
                "LEFT JOIN course_learning_outcome AS clo ON clo.plo_code = plo.code AND clo.course_information_id = :courseInformationId\n" +
                "WHERE clo.id IS NULL";

        List<ProgrammeLearningOutcome> programmeLearningOutcomeList = Ebean.findNative(ProgrammeLearningOutcome.class, sql)
                .setParameter("courseInformationId", courseInformationId)
                .findList();

        if(programmeLearningOutcomeList.size() > 0) {
            logger.debug("List size : " + programmeLearningOutcomeList.size());
            return programmeLearningOutcomeList;
        }
        else {
            return new ArrayList<>();
        }
    }

    public boolean hasCloToPloMap(String cloTitle, String ploCode, Long lecturerId, Long courseInformationId) {
        int count = Ebean.find(CourseLearningOutcome.class).where().and()
                .eq("title", cloTitle)
                .eq("ploCode", ploCode)
                .eq("lecturerId", lecturerId)
                .eq("courseInformationId", courseInformationId)
                .endAnd().findCount();

        if(count == 0) {
            count = Ebean.find(CourseLearningOutcome.class).where().and()
                    .eq("title", cloTitle)
                    .eq("lecturerId", lecturerId)
                    .eq("courseInformationId", courseInformationId)
                    .endAnd().findCount();
        }

        if(count == 0) {
            count = Ebean.find(CourseLearningOutcome.class).where().and()
                    .eq("ploCode", ploCode)
                    .eq("lecturerId", lecturerId)
                    .eq("courseInformationId", courseInformationId)
                    .endAnd().findCount();
        }

        return count > 0;
    }

    public int countCloToPloMaps(Long lecturerId, Long courseInformationId) {

        return Ebean.find(CourseLearningOutcome.class).where().and()
                .eq("lecturerId", lecturerId)
                .eq("courseInformationId", courseInformationId)
                .endAnd().findCount();
    }

    public void saveCloToPloMap(String cloTitle, String ploCode, Long lecturerId, Long courseInformationId) {
        CourseLearningOutcome courseLearningOutcome = new CourseLearningOutcome();
        courseLearningOutcome.title = cloTitle;
        courseLearningOutcome.ploCode = ploCode;
        courseLearningOutcome.lecturerId = lecturerId;
        courseLearningOutcome.courseInformationId = courseInformationId;
        Ebean.save(courseLearningOutcome);
    }

    public void deleteCloToPloMap(Long cloToPloMapId) {
        int delete = Ebean.delete(CourseLearningOutcome.class, cloToPloMapId);
        logger.debug("Deleted : " + delete);
    }

    public List<CourseLearningOutcome> getCourseLearningOutcomeList(Long courseInformationId, Long lecturerId) {
        List<CourseLearningOutcome> courseLearningOutcomes = Ebean.find(CourseLearningOutcome.class).where().and().eq("lecturerId", lecturerId)
                .eq("courseInformationId", courseInformationId)
                .endAnd().orderBy().asc("ploCode").findList();

        if(courseLearningOutcomes.size() > 0) {
            return courseLearningOutcomes;
        }
        else {
            return new ArrayList<>();
        }
    }

    public void saveAssessmentInfo(String assessment, String assessmentType, int fullMarks, int weightage, String cloTitle,
                                   Long lecturerId, Long courseInformationId) {

        AssessmentInfo info = new AssessmentInfo();
        info.assessment = assessment;
        info.assessmentType = assessmentType;
        info.fullMarks = fullMarks;
        info.weightage = weightage;
        info.cloTitle = cloTitle;
        info.lecturerId = lecturerId;
        info.courseInformationId = courseInformationId;

        Ebean.save(info);
    }

    public List<AssessmentInfo> getAssessmentInfoList(Long lecturerId, Long courseId) {
        return Ebean.find(AssessmentInfo.class).where().and()
                .eq("lecturerId", lecturerId)
                .eq("courseInformationId", courseId)
                .endAnd().findList();
    }

    public Long getTotalAssessmentWeights(Long lecturerId, Long courseId) {
        String sql = "SELECT SUM(weightage) \n" +
                "FROM assessment_info \n" +
                "WHERE lecturer_id = :lecturerId AND course_information_id = :courseInformationId";

        return Ebean.findNative(ProgrammeLearningOutcome.class, sql)
                .setParameter("lecturerId", lecturerId)
                .setParameter("courseInformationId", courseId)
                .findSingleAttribute();
    }
}
