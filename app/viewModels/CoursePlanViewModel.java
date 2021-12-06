package viewModels;

import models.CourseInformation;
import models.CourseLearningOutcome;
import models.Lecturer;
import models.ProgrammeLearningOutcome;

import java.util.List;
import java.util.stream.Collectors;

public class CoursePlanViewModel {

    public String programme;
    public String courseCode;
    public String courseName;
    public String semester;
    public String intakeBatch;
    public String lecturerName;
    public String courseType;
    public List<Plo> ploList;
    public List<CloToPloMapper> cloToPloMapperList;

    public static CoursePlanViewModel build(CourseInformation courseInformation, Lecturer lecturer,
                                            List<ProgrammeLearningOutcome> programmeLearningOutcomeList,
                                            List<CourseLearningOutcome> courseLearningOutcomes) {

        String lecturerName = lecturer.firstName + " " + lecturer.lastName;

        CoursePlanViewModel coursePlanViewModel = new CoursePlanViewModel();
        coursePlanViewModel.programme = courseInformation.programme;
        coursePlanViewModel.courseCode = courseInformation.courseCode;
        coursePlanViewModel.courseName = courseInformation.courseName;
        coursePlanViewModel.semester = courseInformation.semester;
        coursePlanViewModel.intakeBatch = courseInformation.intakeBatch;
        coursePlanViewModel.lecturerName = lecturerName;
        coursePlanViewModel.courseType = courseInformation.courseType;

        coursePlanViewModel.ploList = programmeLearningOutcomeList.stream().map(item -> new Plo(item.title, item.code))
                .collect(Collectors.toList());

        coursePlanViewModel.cloToPloMapperList = courseLearningOutcomes.stream().map(item -> {
            ProgrammeLearningOutcome plo = programmeLearningOutcomeList.stream()
                    .filter(ploListItem -> item.ploCode.equals(ploListItem.code))
                    .findAny()
                    .orElse(null);

            if(plo != null) {
                return new CloToPloMapper(plo.title, plo.code, item.title);
            }
            else {
                return null;
            }

        }).collect(Collectors.toList());

        return coursePlanViewModel;
    }

    public static class Plo {
        public String title;
        public String code;

        public Plo(String title, String code) {
            this.title = title;
            this.code = code;
        }
    }

    public static class CloToPloMapper {
        public String ploTitle;
        public String ploCode;
        public String cloTitle;

        public CloToPloMapper(String ploTitle, String ploCode, String cloTitle) {
            this.ploTitle = ploTitle;
            this.ploCode = ploCode;
            this.cloTitle = cloTitle;
        }
    }
}
