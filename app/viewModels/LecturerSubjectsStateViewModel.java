package viewModels;

import controllers.routes;
import models.LecturerCurrentSubject;
import play.mvc.Call;

import java.util.List;
import java.util.stream.Collectors;

public class LecturerSubjectsStateViewModel {

    public String courseName;
    public Call currentPageUrl;
    public Boolean isCompleted;

    public static List<LecturerSubjectsStateViewModel> to(List<LecturerCurrentSubject> list) {
        return list.stream().map(item -> {
            LecturerSubjectsStateViewModel viewModel = new LecturerSubjectsStateViewModel();
            viewModel.courseName = item.courseName;
            viewModel.isCompleted = item.isCompleted;

            viewModel.currentPageUrl = getCurrentPageUrl(item.currentPage, item.lecturerId, item.courseInformationId);
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
}
