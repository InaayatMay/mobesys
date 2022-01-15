package services;

import io.ebean.Ebean;
import io.ebean.RawSql;
import io.ebean.RawSqlBuilder;
import io.ebean.annotation.Transactional;
import models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.List;

public class RequestService {
    private final Logger logger = LoggerFactory.getLogger("application");

    public void saveRequest(Long toLecturerId, Long fromLecturerId, Long courseId, String message) {
        Request request = new Request();
        request.fromLecturerId = fromLecturerId;
        request.toLecturerId = toLecturerId;
        request.courseInformationId = courseId;
        request.request = message;
        request.createdAt = new Timestamp(System.currentTimeMillis());
        request.isApproved = false;
        Ebean.save(request);
    }

    public List<RequestAggregate> getLecturerRequestAggregateList(Long lecturerId) {
        String sql = "select req.id, req.from_lecturer_id, concat(fr.first_name, ' ', fr.last_name) as request_by, req.to_lecturer_id, \n" +
                "concat(toL.first_name, ' ', toL.last_name) as request_to, \n" +
                "req.course_information_id, ci.course_code, ci.course_name, req.request, req.is_approved, req.created_at, req.approved_at\n" +
                "from request as req\n" +
                "left join course_information as ci on ci.id = req.course_information_id\n" +
                "left join lecturer as fr on req.from_lecturer_id = fr.id\n" +
                "left join lecturer as toL on req.to_lecturer_id = toL.id\n" +
                "where req.from_lecturer_id = :lecturerId;";

        RawSql rawSql = RawSqlBuilder.parse(sql)
                .columnMapping("req.from_lecturer_id", "requestById")
                .columnMapping("request_by", "requestByName")
                .columnMapping("req.to_lecturer_id", "requestToId")
                .columnMapping("request_to", "requestToName")
                .create();

        return Ebean.find(RequestAggregate.class).setRawSql(rawSql)
                .setParameter("lecturerId", lecturerId).findList();
    }

    public List<RequestAggregate> getRequestAggregateListForLecturer(Long lecturerId) {
        String sql = "select req.id, req.from_lecturer_id, concat(fr.first_name, ' ', fr.last_name) as request_by, req.to_lecturer_id, \n" +
                "concat(toL.first_name, ' ', toL.last_name) as request_to, \n" +
                "req.course_information_id, ci.course_code, ci.course_name, req.request, req.is_approved, req.created_at, req.approved_at\n" +
                "from request as req\n" +
                "left join course_information as ci on ci.id = req.course_information_id\n" +
                "left join lecturer as fr on req.from_lecturer_id = fr.id\n" +
                "left join lecturer as toL on req.to_lecturer_id = toL.id\n" +
                "where req.to_lecturer_id = :lecturerId;";

        RawSql rawSql = RawSqlBuilder.parse(sql)
                .columnMapping("req.from_lecturer_id", "requestById")
                .columnMapping("request_by", "requestByName")
                .columnMapping("req.to_lecturer_id", "requestToId")
                .columnMapping("request_to", "requestToName")
                .create();

        return Ebean.find(RequestAggregate.class).setRawSql(rawSql)
                .setParameter("lecturerId", lecturerId).findList();
    }

    public void approveRequest(Long requestId, Long lecturerId) {
        Ebean.beginTransaction();
        {
            Request request = Ebean.find(Request.class, requestId);
            request.isApproved = true;
            request.approvedAt = new Timestamp(System.currentTimeMillis());
            Ebean.update(request);

            List<AssessmentInfo> assessmentInfos = Ebean.find(AssessmentInfo.class)
                    .where().and()
                    .eq("lecturerId", lecturerId)
                    .eq("courseInformationId", request.courseInformationId)
                    .endAnd().findList();
            for(AssessmentInfo assessmentInfo: assessmentInfos) {
                assessmentInfo.lecturerId = request.fromLecturerId;
                Ebean.update(assessmentInfo);
            }

            LecturerCourseMap lecturerCourseMap = Ebean.find(LecturerCourseMap.class)
                    .where().and()
                    .eq("lecturerId", lecturerId)
                    .eq("courseInformationId", request.courseInformationId)
                    .endAnd().findOne();
            if(lecturerCourseMap != null) {
                lecturerCourseMap.lecturerId = request.fromLecturerId;
                Ebean.update(lecturerCourseMap);
            }

            List<CourseLearningOutcome> courseLearningOutcomes = Ebean.find(CourseLearningOutcome.class)
                    .where().and()
                    .eq("lecturerId", lecturerId)
                    .eq("courseInformationId", request.courseInformationId)
                    .endAnd().findList();
            for(CourseLearningOutcome courseLearningOutcome: courseLearningOutcomes) {
                courseLearningOutcome.lecturerId = request.fromLecturerId;
                Ebean.update(courseLearningOutcome);
            }

            LecturerCurrentSubject lecturerCurrentSubject = Ebean.find(LecturerCurrentSubject.class)
                    .where().and()
                    .eq("lecturerId", lecturerId)
                    .eq("courseInformationId", request.courseInformationId)
                    .endAnd().findOne();
            if (lecturerCurrentSubject != null) {
                lecturerCurrentSubject.lecturerId = request.fromLecturerId;
                Ebean.update(lecturerCurrentSubject);
            }

            List<PreviousCloRecord> previousCloRecords = Ebean.find(PreviousCloRecord.class)
                    .where().and()
                    .eq("lecturerId", lecturerId)
                    .eq("courseInformationId", request.courseInformationId)
                    .endAnd().findList();
            for(PreviousCloRecord previousCloRecord: previousCloRecords) {
                previousCloRecord.lecturerId = request.fromLecturerId;
                Ebean.update(lecturerId);
            }

            List<StudentToCourseMap> studentToCourseMapList = Ebean.find(StudentToCourseMap.class)
                    .where().eq("courseInformationId", request.courseInformationId).findList();
            for(StudentToCourseMap studentToCourseMap: studentToCourseMapList) {
                Student student = Ebean.find(Student.class, studentToCourseMap.id);
                if (student != null) {
                    student.lecturerId = request.fromLecturerId;
                    Ebean.update(student);
                }
            }

            List<StudentMarks> studentMarksList = Ebean.find(StudentMarks.class)
                    .where().and()
                    .eq("lecturerId", lecturerId)
                    .eq("courseInformationId", request.courseInformationId)
                    .endAnd().findList();
            for(StudentMarks studentMarks: studentMarksList) {
                studentMarks.lecturerId = request.fromLecturerId;
                Ebean.update(studentMarks);
            }

            List<StudentAttemptNumber> studentAttemptNumbers = Ebean.find(StudentAttemptNumber.class)
                    .where().and()
                    .eq("lecturerId", lecturerId)
                    .eq("courseInformationId", request.courseInformationId)
                    .endAnd().findList();
            for(StudentAttemptNumber studentAttemptNumber: studentAttemptNumbers) {
                studentAttemptNumber.lecturerId = request.fromLecturerId;
                Ebean.update(studentAttemptNumber);
            }

            Ebean.commitTransaction();
        }
        Ebean.endTransaction();

    }
}
