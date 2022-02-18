package services;

import io.ebean.Ebean;
import io.ebean.RawSql;
import io.ebean.RawSqlBuilder;
import models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

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

    public LecturerCourseMap getLecturerCourseMap(Long lecturerId, Long courseId) {
        return Ebean.find(LecturerCourseMap.class).where()
                .and()
                .eq("lecturer_id", lecturerId)
                .eq("course_information_id", courseId)
                .endAnd()
                .findOne();
    }

    public List<School> getSchoolList() {
        return Ebean.find(School.class).findList();
    }

    public Department getDepartment(Long id) {
        return Ebean.find(Department.class, id);
    }

    public List<Department> getDepartmentListBySchool(Long schoolId) {
        return Ebean.find(Department.class).where().eq("school_id", schoolId).findList();
    }

    public List<String> getProgrammesByDepartment(Long deptId) {
        String sql = "select programme\n" +
                "from course_information\n" +
                "where department_id = :deptId\n" +
                "group by programme";

        return Ebean.findNative(CourseInformation.class, sql)
                .setParameter("deptId", deptId)
                .findSingleAttributeList();
    }

    public List<CourseInformation> getCourseInformationByDept(String programme) {
        String sql = "select course_information.*\n" +
                "from course_information\n" +
                "left join lecturer_course_map on lecturer_course_map.course_information_id = course_information.id\n" +
                "where programme = :programme and lecturer_course_map.id is null;";

        return Ebean.findNative(CourseInformation.class, sql)
                .setParameter("programme", programme)
                .findList();
    }

    public CourseInformation getCourseInformationById(Long courseInformationId) {
        return Ebean.find(CourseInformation.class).where().eq("id", courseInformationId).findOne();
    }

    public List<ProgrammeLearningOutcome> getProgrammeLearningOutcomeList() {
        return Ebean.find(ProgrammeLearningOutcome.class).findList();
    }

    public List<ProgrammeLearningOutcome> getUnlinkedPloList(Long courseInformationId) {
        CourseInformation courseInformation = getCourseInformationById(courseInformationId);
        String sql;
        if(courseInformation.programme.equals("Bachelor of Computer Science (Hons)")) {
            sql = "SELECT plo.*\n" +
                    "FROM programme_learning_outcome AS plo\n" +
                    "LEFT JOIN course_learning_outcome AS clo ON clo.plo_code = plo.code AND clo.course_information_id = :courseInformationId\n" +
                    "WHERE clo.id IS NULL AND plo.id <= 9";
        }
        else {
            sql = "SELECT plo.*\n" +
                    "FROM programme_learning_outcome AS plo\n" +
                    "LEFT JOIN course_learning_outcome AS clo ON clo.plo_code = plo.code AND clo.course_information_id = :courseInformationId\n" +
                    "WHERE clo.id IS NULL";
        }

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

    public List<String> getUnlinkedCloList(Long courseInformationId) {
        List<String> list = Arrays.asList("CLO1", "CLO2", "CLO3", "CLO4", "CLO5", "CLO6", "CLO7", "CLO8");

        String sql = "SELECT code\n" +
                "FROM course_learning_outcome\n" +
                "WHERE course_information_id = :courseInformationId";

        List<CourseLearningOutcome> linkedCloList = Ebean.find(CourseLearningOutcome.class).where()
                .eq("course_information_id", courseInformationId).findList();

        List<String> linkedCloCodeList = linkedCloList.stream().map(clo -> clo.code).collect(Collectors.toList());

        List<String> differences = new ArrayList<>(list);
        differences.removeAll(linkedCloCodeList);

        return differences;
    }

    public List<Long> cloToPloDuplicateList(String cloCode, String cloTitle, String ploCode, Long lecturerId,
                                            Long courseInformationId) {
        return Ebean.find(CourseLearningOutcome.class).where().and()
                .eq("code", cloCode)
                .eq("title", cloTitle)
                .eq("ploCode", ploCode)
                .eq("lecturerId", lecturerId)
                .eq("courseInformationId", courseInformationId)
                .endAnd().findIds();
    }

    public List<Long> ploDuplicateList(String ploCode, Long lecturerId, Long courseInformationId) {
        return Ebean.find(CourseLearningOutcome.class).where().and()
                .eq("ploCode", ploCode)
                .eq("lecturerId", lecturerId)
                .eq("courseInformationId", courseInformationId)
                .endAnd().findIds();
    }

    public List<Long> cloDuplicateList(String cloCode, String cloTitle, Long lecturerId, Long courseInformationId) {
        List<Long> cloCodeDuplicateList = Ebean.find(CourseLearningOutcome.class).where()
                .and()
                .eq("code", cloCode)
                .eq("lecturerId", lecturerId)
                .eq("courseInformationId", courseInformationId)
                .endAnd()
                .findIds();

        List<Long> cloTitleDuplicateList = Ebean.find(CourseLearningOutcome.class).where()
                .and()
                .eq("title", cloTitle)
                .eq("lecturerId", lecturerId)
                .eq("courseInformationId", courseInformationId)
                .endAnd()
                .findIds();

        Set<Long> set = new LinkedHashSet<>(cloCodeDuplicateList);
        set.addAll(cloTitleDuplicateList);
        return new ArrayList<>(set);
    }

    public int countCloToPloMaps(Long lecturerId, Long courseInformationId) {

        return Ebean.find(CourseLearningOutcome.class).where().and()
                .eq("lecturerId", lecturerId)
                .eq("courseInformationId", courseInformationId)
                .endAnd().findCount();
    }

    public void saveCloToPloMap(String cloCode, String cloTitle, String ploCode, Long lecturerId, Long courseInformationId) {
        CourseLearningOutcome courseLearningOutcome = new CourseLearningOutcome();
        courseLearningOutcome.code = cloCode;
        courseLearningOutcome.title = cloTitle;
        courseLearningOutcome.ploCode = ploCode;
        courseLearningOutcome.lecturerId = lecturerId;
        courseLearningOutcome.courseInformationId = courseInformationId;
        Ebean.save(courseLearningOutcome);
    }

    public void updateCloToPloMap(String cloCode, String cloTitle, String ploCode, Long courseLearningOutcomeId) {
        CourseLearningOutcome courseLearningOutcome = getCourseLearningOutcome(courseLearningOutcomeId);
        courseLearningOutcome.code = cloCode;
        courseLearningOutcome.title = cloTitle;
        courseLearningOutcome.ploCode = ploCode;
        Ebean.update(courseLearningOutcome);
    }

    public void deleteCloToPloMap(Long cloToPloMapId) {
        int delete = Ebean.delete(CourseLearningOutcome.class, cloToPloMapId);
        logger.debug("Deleted : " + delete);
    }

    public List<CourseLearningOutcome> getCourseLearningOutcomeList(Long courseInformationId, Long lecturerId) {
        return Ebean.find(CourseLearningOutcome.class).where().and().eq("lecturerId", lecturerId)
                .eq("courseInformationId", courseInformationId)
                .endAnd()
                //.orderBy("substr(plo_code from 1 for 3), cast(substr(plo_code from 4) AS UNSIGNED)")
                .orderBy("substring(plo_code, 1, 3), CAST(substring(plo_code, 4) AS INTEGER)")
                .findList();
    }

    public CourseLearningOutcome getCourseLearningOutcome(Long id) {
        return Ebean.find(CourseLearningOutcome.class, id);
    }

    public ProgrammeLearningOutcome getProgrammeLearningOutcome(String code) {
        return Ebean.find(ProgrammeLearningOutcome.class).where().eq("code", code).findOne();
    }

    public void saveAssessmentInfo(String assessment, String assessmentType, int fullMarks, Double weightage, String cloTitle,
                                   Long lecturerId, Long courseInformationId) {

        AssessmentInfo info = new AssessmentInfo();
        info.assessment = assessment;
        info.assessmentType = assessmentType;
        info.fullMarks = fullMarks;
        info.weightage = weightage;
        info.cloCode = cloTitle;
        info.lecturerId = lecturerId;
        info.courseInformationId = courseInformationId;

        Ebean.save(info);
    }

    public List<AssessmentInfo> getAssessmentInfoList(Long lecturerId, Long courseId) {
        return Ebean.find(AssessmentInfo.class).where().and()
                .eq("lecturerId", lecturerId)
                .eq("courseInformationId", courseId)
                .endAnd()
                .orderBy().asc("id").findList();
    }

    public AssessmentInfo getAssessmentInfo(Long id) {
        return Ebean.find(AssessmentInfo.class, id);
    }

    public void updateAssessmentInfo(AssessmentInfo assessmentInfo) {
        Ebean.update(assessmentInfo);
    }

    public Double getTotalAssessmentWeights(Long lecturerId, Long courseId) {
        String sql = "SELECT SUM(weightage) \n" +
                "FROM assessment_info \n" +
                "WHERE lecturer_id = :lecturerId AND course_information_id = :courseInformationId";

        Long total =  Ebean.findNative(ProgrammeLearningOutcome.class, sql)
                .setParameter("lecturerId", lecturerId)
                .setParameter("courseInformationId", courseId)
                .findSingleAttribute();

        return total == null ? 0.0 : Double.parseDouble(String.valueOf(total));
    }

    public List<CloWithTotalWeightage> getCloWithTotalWeights(Long lecturerId, Long courseId) {
        String sql = "SELECT clo_code, plo_code, SUM(full_marks) AS total_full_marks, ROUND(SUM(weightage), 2) AS total_weightage\n" +
                "FROM assessment_info as aif\n" +
                "left join course_learning_outcome as clo on clo.course_information_id = :courseInformationId and clo.lecturer_id = :lecturerId and aif.clo_code = clo.code\n" +
                "WHERE aif.lecturer_id = :lecturerId AND aif.course_information_id = :courseInformationId\n" +
                "GROUP BY clo_code, clo.plo_code;";

        return Ebean.findDto(CloWithTotalWeightage.class, sql)
                .setParameter("lecturerId", lecturerId)
                .setParameter("courseInformationId", courseId)
                .findList();
    }

    public void deleteAssessment(Long assessmentId) {
        int delete = Ebean.delete(AssessmentInfo.class, assessmentId);
        logger.debug("Deleted : " + delete);
    }

    public void deleteAllAssessments(Long lecturerId, Long courseId) {
        try{
            List<AssessmentInfo> assessmentInfoList = getAssessmentInfoList(lecturerId, courseId);
            Ebean.beginTransaction();

            Ebean.deleteAll(assessmentInfoList);

            Ebean.commitTransaction();
        }catch(Exception e){
            Ebean.endTransaction();
        }
    }

    public Optional<PreviousCloRecord> getPreviousCloRecord(Long lecturerId, Long courseId, String cloCode) {
        return Ebean.find(PreviousCloRecord.class).where().and()
                .eq("lecturerId", lecturerId)
                .eq("courseInformationId", courseId)
                .eq("cloCode", cloCode)
                .endAnd().findOneOrEmpty();
    }

    public List<PreviousCloRecord> getPreviousCloRecordList(Long lecturerId, Long courseId) {
        return Ebean.find(PreviousCloRecord.class).where().and()
                .eq("lecturerId", lecturerId)
                .eq("courseInformationId", courseId).findList();
    }

    public void savePreviousCloRecord(Long lecturerId, Long courseId, String cloCode, Optional<Double> previousSemesterClassAvg,
                                      Optional<String> comments) {

        Optional<PreviousCloRecord> optional = getPreviousCloRecord(lecturerId, courseId, cloCode);
        if(optional.isPresent()) {
            PreviousCloRecord previousCloRecord = optional.get();
            previousSemesterClassAvg.ifPresent(aDouble -> previousCloRecord.previousSemesterClassAverage = aDouble);
            comments.ifPresent(s -> previousCloRecord.comments = s);
            Ebean.update(previousCloRecord);
        }
        else {
            PreviousCloRecord previousCloRecord = new PreviousCloRecord();
            previousCloRecord.lecturerId = lecturerId;
            previousCloRecord.courseInformationId = courseId;
            previousCloRecord.cloCode = cloCode;
            previousCloRecord.previousSemesterClassAverage = previousSemesterClassAvg.orElse(null);
            previousCloRecord.comments = comments.orElse( null);
            Ebean.save(previousCloRecord);
        }
    }

    public List<CourseInformation> getCourseInformationsByCourseName(String courseName) {
        return Ebean.find(CourseInformation.class).where().eq("course_name", courseName).findList();
    }

    public Double getTotalWeightageByAssessmentType(Long lecturerId, Long courseId, String assessmentType) {
        String sql = "select sum(weightage)\n" +
                "from assessment_info\n" +
                "where lecturer_id = :lecturerId and course_information_id = :courseInformationId and assessment_type = :assessmentType\n" +
                "group by assessment_type;";

        Long weightage = Ebean.findNative(AssessmentInfo.class, sql)
                .setParameter("lecturerId", lecturerId)
                .setParameter("courseInformationId", courseId)
                .setParameter("assessmentType", assessmentType)
                .findSingleAttribute();

        return weightage == null ? 0.0 : Double.parseDouble(String.valueOf(weightage));
    }

    public boolean isWeightageExceed(String assessmentType, Double newWeightage) {
        if(assessmentType.equals("Assignment") || assessmentType.equals("Mini Project")) {
            return newWeightage > 30;
        }
        else if(assessmentType.equals("Test-1") || assessmentType.equals("Test-2")) {
            return newWeightage > 20;
        }
        else if(assessmentType.equals("Quiz")) {
            return newWeightage > 15;
        }
        else if(assessmentType.equals("Final Exam")) {
            return newWeightage > 50;
        }
        else return false;
    }

    public boolean isWeightageExceedByCourseWork(Long lecturerId, Long courseId, Optional<AssessmentInfo> assessmentInfoOptional, Double newWeightage) {
        String sql = "select sum(weightage)\n" +
                "from assessment_info\n" +
                "where lecturer_id = :lecturerId and course_information_id = :courseInformationId and assessment_type <> 'Final Exam';";

        Long weightage = Ebean.findNative(AssessmentInfo.class, sql)
                .setParameter("lecturerId", lecturerId)
                .setParameter("courseInformationId", courseId)
                .findSingleAttribute();


        Double weightageDouble;
        if(weightage == null) {
            weightageDouble = 0.0;
        }
        else {
            weightageDouble = weightage * 1.0;
        }

        if(assessmentInfoOptional.isPresent()) {
            weightageDouble = weightageDouble - assessmentInfoOptional.get().weightage;
        }

        return weightageDouble + newWeightage > 50;
    }

    public boolean isWeightageExceedByFinalExam(Long lecturerId, Long courseId, Optional<AssessmentInfo> assessmentInfoOptional, Double newWeightage) {
        String sql = "select sum(weightage)\n" +
                "from assessment_info\n" +
                "where lecturer_id = :lecturerId and course_information_id = :courseInformationId and assessment_type = 'Final Exam';";

        Long weightage = Ebean.findNative(AssessmentInfo.class, sql)
                .setParameter("lecturerId", lecturerId)
                .setParameter("courseInformationId", courseId)
                .findSingleAttribute();

        Double weightageDouble;
        if(weightage == null) {
            weightageDouble = 0.0;
        }
        else {
            weightageDouble = weightage * 1.0;
        }

        if(assessmentInfoOptional.isPresent()) {
            weightageDouble = weightageDouble - assessmentInfoOptional.get().weightage;
        }

        return weightageDouble + newWeightage > 50;
    }

    public Double getDefaultAssessmentWeightage(String assessmentType) {
        if(assessmentType.equals("Assignment") || assessmentType.equals("Mini Project")) {
            return 30.0;
        }
        else if(assessmentType.equals("Test-1") || assessmentType.equals("Test-2")) {
            return 20.0;
        }
        else if(assessmentType.equals("Quiz")) {
            return 15.0;
        }
        else {
            return 50.0;
        }
    }

    public List<CourseInformation> getCourseInformationListByLecturer(Long lecturerId) {
        String sql = "select course_information.id, course_information.programme, course_information.course_code, " +
                "course_information.course_name, course_information.semester, course_information.intake_batch," +
                "course_information.course_type, course_information.department_id\n" +
                "from lecturer_course_map\n" +
                "left join course_information on course_information.id = lecturer_course_map.course_information_id\n" +
                "where lecturer_course_map.lecturer_id = :lecturerId;";

        RawSql rawSql = RawSqlBuilder.parse(sql)
                .columnMapping("course_information.id", "id")
                .create();

        return Ebean.find(CourseInformation.class).setRawSql(rawSql)
                .setParameter("lecturerId", lecturerId).findList();
    }

    public List<CourseInformation> getCompletedCourseInformationListByLecturer(Long lecturerId) {
        String sql = "select course_information.id, course_information.programme, course_information.course_code, " +
                "course_information.course_name, course_information.semester, course_information.intake_batch," +
                "course_information.course_type, course_information.department_id\n" +
                "from lecturer_current_subject\n" +
                "left join course_information on course_information.id = lecturer_current_subject.course_information_id\n" +
                "where is_completed = true and lecturer_current_subject.lecturer_id = :lecturerId;";

        RawSql rawSql = RawSqlBuilder.parse(sql)
                .columnMapping("course_information.id", "id")
                .create();

        return Ebean.find(CourseInformation.class).setRawSql(rawSql)
                .setParameter("lecturerId", lecturerId).findList();
    }
}
