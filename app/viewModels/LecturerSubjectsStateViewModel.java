package viewModels;

import controllers.routes;
import io.ebean.Ebean;
import models.CourseInformation;
import models.LecturerCurrentSubject;
import play.mvc.Call;

import java.util.List;
import java.util.stream.Collectors;

public class LecturerSubjectsStateViewModel {

    public String courseName;
    public String programmeShortForm;
    public String programme;
    public String courseCode;
    public Call currentPageUrl;
    public Boolean isCompleted;
    public int completedPercent;

    public static List<LecturerSubjectsStateViewModel> to(List<LecturerCurrentSubject> list) {
        return list.stream().map(item -> {
            LecturerSubjectsStateViewModel viewModel = new LecturerSubjectsStateViewModel();
            viewModel.courseName = item.courseName;
            viewModel.isCompleted = item.isCompleted;

            CourseInformation courseInformation = Ebean.find(CourseInformation.class, item.courseInformationId);
            viewModel.programmeShortForm = getProgrammeShortForm(courseInformation.programme);
            viewModel.programme = courseInformation.programme;
            viewModel.courseCode = courseInformation.courseCode;
            viewModel.currentPageUrl = getCurrentPageUrl(item.currentPage, item.lecturerId, item.courseInformationId);
            viewModel.completedPercent = getCompletedPercent(item.currentPage, item.isCompleted);
            return viewModel;
        }).collect(Collectors.toList());
    }

    public static Call getCurrentPageUrl(String currentPage, Long lecturerId, Long courseInformationId) {
        switch (currentPage) {
            case "Course Plan":
                return routes.CourseInformationController.showCourseInformationDetails(lecturerId, courseInformationId);
            case "Assessment":
                return routes.CourseInformationController.showAssessmentInformationForm(lecturerId, courseInformationId);
            case "Student":
                return routes.CourseInformationController.showStudentInformationForm(lecturerId, courseInformationId);
            default:
                return routes.CourseInformationController.showReports(lecturerId, courseInformationId);
        }
    }

    public static int getCompletedPercent(String currentPage, boolean isCompleted) {
        switch (currentPage) {
            case "Course Plan": return 50;
            case "Assessment": return 75;
            default: {
                if(isCompleted) {
                    return 100;
                }
                else {
                    return 75;
                }
            }
        }
    }

    public static String getProgrammeShortForm(String programme) {
        switch (programme) {
            case "Bachelor of Computer Science (Hons)":
                return "BCS";
            case "Bachelor of Computer Engineering (Hons)":
                return "BCE";
            default:
                return "BCVE";
        }
    }
}
