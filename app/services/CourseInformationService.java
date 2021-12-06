package services;

import io.ebean.Ebean;
import models.CourseInformation;
import models.CourseLearningOutcome;
import models.ProgrammeLearningOutcome;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CourseInformationService {

    public CourseInformation saveCourseInformation(String programme, String courseCode, String courseName, String semester, String intakeBatch,
                       Long lecturerId, String courseType) {

        CourseInformation courseInformation = new CourseInformation();
        courseInformation.programme = programme;
        courseInformation.courseCode = courseCode;
        courseInformation.courseName = courseName;
        courseInformation.semester = semester;
        courseInformation.intakeBatch = intakeBatch;
        courseInformation.lecturerId = lecturerId;
        courseInformation.courseType = courseType;

        Ebean.save(courseInformation);

        savePlosByCourse(courseInformation);

        return courseInformation;
    }

    public CourseInformation getCourseInformationById(Long courseInformationId) {
        return Ebean.find(CourseInformation.class).where().eq("id", courseInformationId).findOne();
    }

    public List<ProgrammeLearningOutcome> getProgrammeLearningOutcomeList(Long courseInformationId, Long lecturerId) {
        return Ebean.find(ProgrammeLearningOutcome.class).where().and().eq("lecturerId", lecturerId)
                .eq("courseInformationId", courseInformationId)
                .endAnd().findList();
    }

    private void savePlosByCourse(CourseInformation courseInformation) {
        List<String> plos = getPlosAccordingToCourseCode(courseInformation.courseCode);

        for(int i=0; i<plos.size(); i++) {
            ProgrammeLearningOutcome programmeLearningOutcome = new ProgrammeLearningOutcome();
            programmeLearningOutcome.title = plos.get(i);
            programmeLearningOutcome.courseInformationId = courseInformation.id;
            programmeLearningOutcome.lecturerId = courseInformation.lecturerId;
            programmeLearningOutcome.code = "PLO" + (i+1);
            Ebean.save(programmeLearningOutcome);
        }
    }

    public void saveCloToPloMap(String cloTitle, String ploCode, Long lecturerId, Long courseInformationId) {
        CourseLearningOutcome courseLearningOutcome = new CourseLearningOutcome();
        courseLearningOutcome.title = cloTitle;
        courseLearningOutcome.ploCode = ploCode;
        courseLearningOutcome.lecturerId = lecturerId;
        courseLearningOutcome.courseInformationId = courseInformationId;
        Ebean.save(courseLearningOutcome);
    }

    private List<String> getPlosAccordingToCourseCode(String courseCode) {
        if(courseCode.equals("ECB 1001")) {
            return Stream.of("[WA1]Engineering Knowledge",
                    "[WA2]Problem Analysis",
                    "[WA3]Design / Development of Solutions",
                    "[WA4]Investigation",
                    "[WA5]Modern Tool Usage",
                    "[WA6]The Engineer and Society",
                    "[WA7]Environment and Sustainability",
                    "[WA8]Ethics",
                    "[WA9]Individual & Team Work",
                    "[WA10]Communication",
                    "[WA11]Project Management & Finance",
                    "[WA12]Life-Long Learning").collect(Collectors.toList());
        }
        else if(courseCode.equals("test")) {
            return Stream.of("plo_test1", "plo_test2").collect(Collectors.toList());
        }

        return null;
    }

    public List<CourseLearningOutcome> getCourseLearningOutcomeList(Long courseInformationId, Long lecturerId) {
        return Ebean.find(CourseLearningOutcome.class).where().and().eq("lecturerId", lecturerId)
                .eq("courseInformationId", courseInformationId)
                .endAnd().findList();
    }
}
