package controllers;

import dtos.CourseInformationDto;
import dtos.DepartmentDto;
import forms.AssessmentInfoFormData;
import forms.CloToPloMapFormData;
import forms.LecturerCourseMapFormData;
import models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.DynamicForm;
import play.i18n.Messages;
import play.i18n.MessagesApi;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.CourseInformationService;
import services.LecturerService;
import viewModels.CoursePlanViewModel;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CourseInformationController extends Controller {

    private final Logger logger = LoggerFactory.getLogger("application");

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
                Messages messages = messagesApi.preferred(request);

                Form<LecturerCourseMapFormData> form = formFactory.form(LecturerCourseMapFormData.class);
                List<School> schoolList = courseInformationService.getSchoolList();

                return ok(views.html.courseInformationForm.render(id, username, form, schoolList, request, messages));
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
                Form<LecturerCourseMapFormData> formData = formFactory.form(LecturerCourseMapFormData.class).bindFromRequest(request);
                if(formData.hasGlobalErrors()) {
                    return redirect(routes.CourseInformationController.showCourseInformationForm(id));
                }
                else {
                    LecturerCourseMapFormData courseInformationFormData = formData.get();
                    LecturerCourseMap lecturerCourseMap = courseInformationService.saveLecturerCourseMap(id,
                            courseInformationFormData.getCourseInformationId(), courseInformationFormData.getDepartmentId(),
                            courseInformationFormData.getSchoolId());


                    return redirect(routes.CourseInformationController.showCourseInformationDetails(id, lecturerCourseMap.courseInformationId));
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
                List<ProgrammeLearningOutcome> programmeLearningOutcomeList = courseInformationService.getProgrammeLearningOutcomeList();
                List<ProgrammeLearningOutcome> unlinkedPloList = courseInformationService.getUnlinkedPloList(courseId);

                logger.debug("plo size : " + programmeLearningOutcomeList.size());
                logger.debug("unlinked list : " + unlinkedPloList.size());
                List<CourseLearningOutcome> courseLearningOutcomes = courseInformationService.getCourseLearningOutcomeList(courseId, lecturerId);

                CoursePlanViewModel viewModel = CoursePlanViewModel.build(courseInformation, lecturer, programmeLearningOutcomeList,
                        courseLearningOutcomes);
                int numberOfCloToPloMaps = courseInformationService.countCloToPloMaps(lecturerId, courseId);
                Form<CloToPloMapFormData> form = formFactory.form(CloToPloMapFormData.class);

                Messages messages = messagesApi.preferred(request);
                return ok(views.html.courseInformationDetails.render(lecturerId, name, viewModel, form, unlinkedPloList,
                        numberOfCloToPloMaps, request, messages));
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
                    String cloTitle = cloToPloMapFormData.getCloTitle();
                    String ploCode = cloToPloMapFormData.getPloCode();

                    boolean hasDuplicate = courseInformationService.hasCloToPloMap(cloTitle, ploCode, lecturerId, courseId);

                    if(!hasDuplicate) {
                        courseInformationService.saveCloToPloMap(cloTitle, ploCode, lecturerId, courseId);
                    }

                    return redirect(routes.CourseInformationController.showCourseInformationDetails(lecturerId, courseId));
                }
            }
        }
        return unauthorized("You are unauthorized to access this page!");
    }

    public Result deleteCloToPloMap(Http.Request request, Long lecturerId, Long courseId, Long cloToPloMapId) {
        Optional<String> optionalSessionIdString = request.session().get("id");
        Optional<String> optionalUsername = request.session().get("username");

        if(optionalSessionIdString.isPresent() && optionalUsername.isPresent()) {
            Long sessionId = Long.parseLong(optionalSessionIdString.get());
            if (sessionId == lecturerId) {
                courseInformationService.deleteCloToPloMap(cloToPloMapId);
                return redirect(routes.CourseInformationController.showCourseInformationDetails(lecturerId, courseId));
            }
        }

        return unauthorized("You are unauthorized to access this page!");
    }

    public Result showDepartmentsBySchool(Http.Request request, Long schoolId) {
        List<Department> departments = courseInformationService.getDepartmentListBySchool(schoolId);
        List<DepartmentDto> departmentDtos = departments.stream().map(DepartmentDto::to).collect(Collectors.toList());

        return ok(Json.toJson(departmentDtos));
    }

    public Result showCoursesByDeptAndSchool(Http.Request request, Long schoolId, Long departmentId) {
        List<CourseInformation> courseInformations = courseInformationService.getCourseInformationByDept(departmentId);
        List<CourseInformationDto> dtos = courseInformations.stream().map(CourseInformationDto::to).collect(Collectors.toList());

        return ok(Json.toJson(dtos));
    }

    public Result showAssessmentInformationForm(Http.Request request, Long lecturerId, Long courseId) {
        Optional<String> optionalSessionIdString = request.session().get("id");
        Optional<String> optionalUsername = request.session().get("username");

        if(optionalSessionIdString.isPresent() && optionalUsername.isPresent()) {
            Long sessionId = Long.parseLong(optionalSessionIdString.get());
            if (sessionId == lecturerId) {
                Lecturer lecturer = lecturerService.getLecturerById(lecturerId);
                String name = lecturer.firstName + " " + lecturer.lastName;

                CourseInformation courseInformation = courseInformationService.getCourseInformationById(courseId);
                List<ProgrammeLearningOutcome> programmeLearningOutcomeList = courseInformationService.getProgrammeLearningOutcomeList();
                List<AssessmentInfo> assessmentInfos = courseInformationService.getAssessmentInfoList(lecturerId, courseId);
                List<CourseLearningOutcome> courseLearningOutcomes = courseInformationService.getCourseLearningOutcomeList(courseId, lecturerId);

                Long totalAssessmentWeights = courseInformationService.getTotalAssessmentWeights(lecturerId, courseId);

                CoursePlanViewModel viewModel = CoursePlanViewModel.build(courseInformation, lecturer, programmeLearningOutcomeList,
                        courseLearningOutcomes);

                Form<AssessmentInfoFormData> form = formFactory.form(AssessmentInfoFormData.class);

                Messages messages = messagesApi.preferred(request);
                return ok(views.html.assessmentInfoForm.render(lecturerId, name, viewModel, form, assessmentInfos,
                        totalAssessmentWeights, request, messages));
            }
        }
        return unauthorized("You are unauthorized to access this page!");
    }

    public Result handleAssessmentInformationForm(Http.Request request, Long lecturerId, Long courseId) {
        Optional<String> optionalSessionIdString = request.session().get("id");
        Optional<String> optionalUsername = request.session().get("username");

        if(optionalSessionIdString.isPresent() && optionalUsername.isPresent()) {
            Long sessionId = Long.parseLong(optionalSessionIdString.get());
            if (sessionId == lecturerId) {
                Form<AssessmentInfoFormData> fromRequest = formFactory.form(AssessmentInfoFormData.class).bindFromRequest(request);
                if(fromRequest.hasGlobalErrors()) {
                    return redirect(routes.CourseInformationController.showAssessmentInformationForm(lecturerId, courseId));
                }
                else {
                    AssessmentInfoFormData formData = fromRequest.get();
                    courseInformationService.saveAssessmentInfo(formData.getAssessment(), formData.getAssessmentType(),
                            formData.getFullMarks(), formData.getWeightage(), formData.getCloTitle(), lecturerId,
                            courseId);

                    return redirect(routes.CourseInformationController.showAssessmentInformationForm(lecturerId, courseId));
                }
            }
        }
        return unauthorized("You are unauthorized to access this page!");
    }
}
