package controllers;

import forms.CloToPloMapFormData;
import forms.CourseInformationFormData;
import models.CourseInformation;
import models.CourseLearningOutcome;
import models.Lecturer;
import models.ProgrammeLearningOutcome;
import play.Logger;
import play.i18n.Messages;
import play.i18n.MessagesApi;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.CourseInformationService;
import services.LecturerService;
import viewModels.CoursePlanViewModel;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

public class CourseInformationController extends Controller {
    private FormFactory formFactory;
    private MessagesApi messagesApi;
    private CourseInformationService courseInformationService;
    private LecturerService lecturerService;

    @Inject
    public CourseInformationController(FormFactory formFactory, MessagesApi messagesApi, CourseInformationService courseInformationService,
                                       LecturerService lecturerService) {
        this.formFactory = formFactory;
        this.messagesApi = messagesApi;
        this.courseInformationService = courseInformationService;
        this.lecturerService = lecturerService;
    }

    public Result index(Http.Request request) {
        return request
                .session()
                .get("name")
                .map(user -> ok("Hello " + user))
                .orElseGet(() -> unauthorized("Oops, something wrong!"));
    }

    public Result showCourseInformationForm(Http.Request request, Long id){
        Optional<String> optionalSessionIdString = request.session().get("id");
        Optional<String> optionalUsername = request.session().get("username");

        if(optionalSessionIdString.isPresent() && optionalUsername.isPresent()) {
            Long sessionId = Long.parseLong(optionalSessionIdString.get());
            if(sessionId == id) {
                String username = optionalUsername.get();
                Form<CourseInformationFormData> form = formFactory.form(CourseInformationFormData.class);
                Messages messages = messagesApi.preferred(request);
                return ok(views.html.courseInformationForm.render(id, username, form, request, messages));
            }
        }
        return unauthorized("You are unauthorized to access this page!");
    }

    public Result handleCourseInformationForm(Http.Request request, Long id) {
        Optional<String> optionalSessionIdString = request.session().get("id");
        Optional<String> optionalUsername = request.session().get("username");

        if(optionalSessionIdString.isPresent() && optionalUsername.isPresent()) {
            Long sessionId = Long.parseLong(optionalSessionIdString.get());
            if(sessionId == id) {
                Form<CourseInformationFormData> formData = formFactory.form(CourseInformationFormData.class).bindFromRequest(request);
                if(formData.hasGlobalErrors()) {
                    return redirect(routes.CourseInformationController.showCourseInformationForm(id));
                }
                else {
                    CourseInformationFormData courseInformationFormData = formData.get();
                    CourseInformation courseInformation = courseInformationService.saveCourseInformation(courseInformationFormData.getProgramme(),
                            courseInformationFormData.getCourseCode(), courseInformationFormData.getCourseName(),
                            courseInformationFormData.getSemester(), courseInformationFormData.getIntakeBatch(), id,
                            courseInformationFormData.getCourseType());

                    return redirect(routes.CourseInformationController.showCourseInformationDetails(id, courseInformation.id));
                }

            }
        }
        return unauthorized("You are unauthorized to access this page!");
    }

    public Result showCourseInformationDetails(Http.Request request, Long lecturerId, Long courseId) {
        Optional<String> optionalSessionIdString = request.session().get("id");
        Optional<String> optionalUsername = request.session().get("username");

        if(optionalSessionIdString.isPresent() && optionalUsername.isPresent()) {
            Long sessionId = Long.parseLong(optionalSessionIdString.get());
            if (sessionId == lecturerId) {
                Lecturer lecturer = lecturerService.getLecturerById(lecturerId);
                String name = lecturer.firstName + " " + lecturer.lastName;

                CourseInformation courseInformation = courseInformationService.getCourseInformationById(courseId);
                List<ProgrammeLearningOutcome> programmeLearningOutcomeList = courseInformationService.getProgrammeLearningOutcomeList(courseId, lecturerId);
                List<CourseLearningOutcome> courseLearningOutcomes = courseInformationService.getCourseLearningOutcomeList(courseId, lecturerId);

                CoursePlanViewModel viewModel = CoursePlanViewModel.build(courseInformation, lecturer, programmeLearningOutcomeList,
                        courseLearningOutcomes);

                Messages messages = messagesApi.preferred(request);
                Form<CloToPloMapFormData> form = formFactory.form(CloToPloMapFormData.class);
                return ok(views.html.courseInformationDetails.render(lecturerId, name, viewModel, form, request, messages));
            }
        }

        return unauthorized("You are unauthorized to access this page!");
    }

    public Result handleCourseInformationDetails(Http.Request request, Long lecturerId, Long courseId) {
        Optional<String> optionalSessionIdString = request.session().get("id");
        Optional<String> optionalUsername = request.session().get("username");

        if(optionalSessionIdString.isPresent() && optionalUsername.isPresent()) {
            Long sessionId = Long.parseLong(optionalSessionIdString.get());
            if (sessionId == lecturerId) {
                Form<CloToPloMapFormData> formData = formFactory.form(CloToPloMapFormData.class).bindFromRequest(request);
                if(formData.hasGlobalErrors()) {
                    return redirect(routes.CourseInformationController.showCourseInformationDetails(lecturerId, courseId));
                }
                else {
                    CloToPloMapFormData cloToPloMapFormData = formData.get();
                    courseInformationService.saveCloToPloMap(cloToPloMapFormData.getCloTitle(), cloToPloMapFormData.getPloCode(),
                            lecturerId, courseId);
                    return redirect(routes.CourseInformationController.showCourseInformationDetails(lecturerId, courseId));
                }
            }
        }
        return unauthorized("You are unauthorized to access this page!");
    }
}
