package controllers;

import dtos.*;
import forms.AssessmentInfoFormData;
import forms.CloToPloMapFormData;
import forms.LecturerCourseMapFormData;
import forms.StudentInfoFormData;
import io.vavr.control.Either;
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
import services.StudentService;
import viewModels.*;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

public class CourseInformationController extends Controller {

    private final Logger logger = LoggerFactory.getLogger("application");

    private FormFactory formFactory;
    private MessagesApi messagesApi;
    private CourseInformationService courseInformationService;
    private LecturerService lecturerService;
    private StudentService studentService;

    @Inject
    public CourseInformationController(FormFactory formFactory, MessagesApi messagesApi, CourseInformationService courseInformationService,
                                       LecturerService lecturerService, StudentService studentService) {
        this.formFactory = formFactory;
        this.messagesApi = messagesApi;
        this.courseInformationService = courseInformationService;
        this.lecturerService = lecturerService;
        this.studentService = studentService;
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
                List<LecturerCurrentSubject> lecturerCurrentSubjectList = lecturerService.getLecturerSubjectsStateList(id);
                List<LecturerSubjectsStateViewModel> subjectsStateViewModels = LecturerSubjectsStateViewModel.to(lecturerCurrentSubjectList);

                return ok(views.html.courseInformationForm.render(id, username, form, schoolList, subjectsStateViewModels, request, messages));
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
                CourseInformation courseInformation = courseInformationService.getCourseInformationById(courseId);
                List<CourseInformation> courseInformationList = courseInformationService.getCourseInformationsByCourseName(courseInformation.courseName);

                List<CourseActivationViewModel> courseActivationViewModels = new ArrayList<>();
                for(CourseInformation courseInfo: courseInformationList) {
                    if(courseInfo.id == courseId) {
                        courseActivationViewModels.add(new CourseActivationViewModel(courseInfo.id, courseInfo.programme, true));
                    }
                    else {
                        courseActivationViewModels.add(new CourseActivationViewModel(courseInfo.id, courseInfo.programme, false));
                    }
                }
                List<ProgrammeLearningOutcome> programmeLearningOutcomeList = courseInformationService.getProgrammeLearningOutcomeList();
                List<ProgrammeLearningOutcome> unlinkedPloList = courseInformationService.getUnlinkedPloList(courseId);
                List<String> unlinkedCloList = courseInformationService.getUnlinkedCloList(courseId);

                logger.debug("plo size : " + programmeLearningOutcomeList.size());
                logger.debug("unlinked list : " + unlinkedPloList.size());
                List<CourseLearningOutcome> courseLearningOutcomes = courseInformationService.getCourseLearningOutcomeList(courseId, lecturerId);

                CoursePlanViewModel viewModel = CoursePlanViewModel.build(courseInformation, lecturer, programmeLearningOutcomeList,
                        courseLearningOutcomes);
                int numberOfCloToPloMaps = courseInformationService.countCloToPloMaps(lecturerId, courseId);
                Form<CloToPloMapFormData> form = formFactory.form(CloToPloMapFormData.class);

                lecturerService.saveLecturerCurrentSubjectState(lecturerId, courseId, courseInformation.courseName, false,
                        "Course Plan");
                List<LecturerCurrentSubject> lecturerCurrentSubjectList = lecturerService.getLecturerSubjectsStateList(lecturerId);
                List<LecturerSubjectsStateViewModel> subjectsStateViewModels = LecturerSubjectsStateViewModel.to(lecturerCurrentSubjectList);

                EssentialFieldsViewModel essentialFieldsViewModel = new EssentialFieldsViewModel(lecturerId, optionalUsername.get(),
                        courseInformation.programme, courseInformation.courseName);

                Messages messages = messagesApi.preferred(request);
                return ok(views.html.courseInformationDetails.render(essentialFieldsViewModel, viewModel, form, unlinkedPloList,
                        numberOfCloToPloMaps, courseActivationViewModels, subjectsStateViewModels, unlinkedCloList, request, messages));
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
                    String cloCode = cloToPloMapFormData.getCloCode();
                    String cloTitle = cloToPloMapFormData.getCloTitle();
                    String ploCode = cloToPloMapFormData.getPloCode();

                    List<Long> cloToPloDuplicateIdList = courseInformationService.cloToPloDuplicateList(cloCode, cloTitle, ploCode,
                            lecturerId, courseId);

                    List<Long> ploDuplicateIdList = courseInformationService.ploDuplicateList(ploCode, lecturerId, courseId);
                    List<Long> cloDuplicateIdList = courseInformationService.cloDuplicateList(cloCode, cloTitle, lecturerId, courseId);

                    if(cloToPloDuplicateIdList.size() > 0 || cloDuplicateIdList.size() > 0 || ploDuplicateIdList.size() > 0) {
                        logger.debug("There is duplication.");
                        Form<CloToPloMapFormData> form;
                        if(cloToPloDuplicateIdList.size() > 0) {
                            form = formFactory.form(CloToPloMapFormData.class).withGlobalError("There is already a map with " + ploCode + " and " + cloTitle + ". Please try again.");
                        }
                        else if(cloDuplicateIdList.size() > 0) {
                            form = formFactory.form(CloToPloMapFormData.class).withGlobalError("There is already a map with " + cloTitle + ". Please try again.");
                        }
                        else {
                            form = formFactory.form(CloToPloMapFormData.class).withGlobalError("There is already a map with " + ploCode + ". Please try again.");
                        }

                        Lecturer lecturer = lecturerService.getLecturerById(lecturerId);

                        CourseInformation courseInformation = courseInformationService.getCourseInformationById(courseId);
                        List<ProgrammeLearningOutcome> programmeLearningOutcomeList = courseInformationService.getProgrammeLearningOutcomeList();
                        List<ProgrammeLearningOutcome> unlinkedPloList = courseInformationService.getUnlinkedPloList(courseId);

                        List<CourseInformation> courseInformationList = courseInformationService.getCourseInformationsByCourseName(courseInformation.courseName);

                        List<CourseActivationViewModel> courseActivationViewModels = new ArrayList<>();
                        for(CourseInformation courseInfo: courseInformationList) {
                            if(courseInfo.id == courseId) {
                                courseActivationViewModels.add(new CourseActivationViewModel(courseInfo.id, courseInfo.programme, true));
                            }
                            else {
                                courseActivationViewModels.add(new CourseActivationViewModel(courseInfo.id, courseInfo.programme, false));
                            }
                        }

                        logger.debug("plo size : " + programmeLearningOutcomeList.size());
                        logger.debug("unlinked list : " + unlinkedPloList.size());
                        List<CourseLearningOutcome> courseLearningOutcomes = courseInformationService.getCourseLearningOutcomeList(courseId, lecturerId);

                        List<String> unlinkedCloList = courseInformationService.getUnlinkedCloList(courseId);


                        CoursePlanViewModel viewModel = CoursePlanViewModel.build(courseInformation, lecturer, programmeLearningOutcomeList,
                                courseLearningOutcomes);
                        int numberOfCloToPloMaps = courseInformationService.countCloToPloMaps(lecturerId, courseId);

                        List<LecturerCurrentSubject> lecturerCurrentSubjectList = lecturerService.getLecturerSubjectsStateList(lecturerId);
                        List<LecturerSubjectsStateViewModel> subjectsStateViewModels = LecturerSubjectsStateViewModel.to(lecturerCurrentSubjectList);

                        EssentialFieldsViewModel essentialFieldsViewModel = new EssentialFieldsViewModel(lecturerId, optionalUsername.get(),
                                courseInformation.programme, courseInformation.courseName);

                        Messages messages = messagesApi.preferred(request);
                        return ok(views.html.courseInformationDetails.render(essentialFieldsViewModel, viewModel,
                                form, unlinkedPloList, numberOfCloToPloMaps, courseActivationViewModels, subjectsStateViewModels,
                                unlinkedCloList, request, messages));
                    }
                    else {
                        courseInformationService.saveCloToPloMap(cloCode, cloTitle, ploCode, lecturerId, courseId);
                        return redirect(routes.CourseInformationController.showCourseInformationDetails(lecturerId, courseId));
                    }
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

    public Result showEditCloToPloMap(Http.Request request, Long lecturerId, Long courseId, Long cloToPloMapId) {
        Optional<String> optionalSessionIdString = request.session().get("id");
        Optional<String> optionalUsername = request.session().get("username");

        if(optionalSessionIdString.isPresent() && optionalUsername.isPresent()) {
            Long sessionId = Long.parseLong(optionalSessionIdString.get());
            if (sessionId == lecturerId) {
                CourseLearningOutcome courseLearningOutcome = courseInformationService.getCourseLearningOutcome(cloToPloMapId);
                List<ProgrammeLearningOutcome> unlinkedPloList = courseInformationService.getUnlinkedPloList(courseId);
                ProgrammeLearningOutcome selectedPlo = courseInformationService.getProgrammeLearningOutcome(courseLearningOutcome.ploCode);
                List<String> unlinkedCloList = courseInformationService.getUnlinkedCloList(courseId);


                Form<CloToPloMapFormData> form = formFactory.form(CloToPloMapFormData.class);
                List<LecturerCurrentSubject> lecturerCurrentSubjectList = lecturerService.getLecturerSubjectsStateList(lecturerId);
                List<LecturerSubjectsStateViewModel> subjectsStateViewModels = LecturerSubjectsStateViewModel.to(lecturerCurrentSubjectList);

                Messages messages = messagesApi.preferred(request);
                return ok(views.html.courseInformationEditForm.render(lecturerId, optionalUsername.get(), form, unlinkedPloList,
                        courseLearningOutcome, selectedPlo, courseId, subjectsStateViewModels, unlinkedCloList, request, messages));
            }
        }
        return unauthorized("You are unauthorized to access this page!");
    }

    public Result handleEditCloToPloMap(Http.Request request, Long lecturerId, Long courseId, Long cloToPloMapId) {
        Optional<String> optionalSessionIdString = request.session().get("id");
        Optional<String> optionalUsername = request.session().get("username");

        if(optionalSessionIdString.isPresent() && optionalUsername.isPresent()) {
            Long sessionId = Long.parseLong(optionalSessionIdString.get());
            if (sessionId == lecturerId) {
                Form<CloToPloMapFormData> formData = formFactory.form(CloToPloMapFormData.class).bindFromRequest(request);
                if(formData.hasGlobalErrors()) {
                    return redirect(routes.CourseInformationController.showEditCloToPloMap(lecturerId, courseId, cloToPloMapId));
                }
                else {
                    CloToPloMapFormData cloToPloMapFormData = formData.get();
                    String cloCode = cloToPloMapFormData.getCloCode();
                    String cloTitle = cloToPloMapFormData.getCloTitle();
                    String ploCode = cloToPloMapFormData.getPloCode();

                    List<Long> cloToPloDuplicateIdList = courseInformationService.cloToPloDuplicateList(cloCode, cloTitle,
                            ploCode, lecturerId, courseId);

                    List<Long> ploDuplicateIdList = courseInformationService.ploDuplicateList(ploCode, lecturerId, courseId);
                    List<Long> cloDuplicateIdList = courseInformationService.cloDuplicateList(cloCode, cloTitle, lecturerId,
                            courseId);

                    boolean hasCloToPloDuplicate = cloToPloDuplicateIdList.size() == 1 && !cloToPloDuplicateIdList.contains(cloToPloMapId);
                    boolean hasCloDuplicate = cloDuplicateIdList.size() == 1 && !cloDuplicateIdList.contains(cloToPloMapId);
                    boolean hasPloDuplicate = ploDuplicateIdList.size() == 1 && !ploDuplicateIdList.contains(cloToPloMapId);

                    if(hasCloToPloDuplicate || hasCloDuplicate || hasPloDuplicate) {
                        logger.debug("there is duplication.");
                        Form<CloToPloMapFormData> form;
                        if(hasCloToPloDuplicate) {
                            form = formFactory.form(CloToPloMapFormData.class).withGlobalError("There is already a map with " + ploCode + " and " + cloCode + ". Please try again.");
                        }
                        else if(hasCloDuplicate) {
                            form = formFactory.form(CloToPloMapFormData.class).withGlobalError("There is already a map with that CLO. Please try again.");
                        }
                        else {
                            form = formFactory.form(CloToPloMapFormData.class).withGlobalError("There is already a map with " + ploCode + ". Please try again.");
                        }

                        CourseLearningOutcome courseLearningOutcome = courseInformationService.getCourseLearningOutcome(cloToPloMapId);
                        List<ProgrammeLearningOutcome> unlinkedPloList = courseInformationService.getUnlinkedPloList(courseId);
                        ProgrammeLearningOutcome selectedPlo = courseInformationService.getProgrammeLearningOutcome(courseLearningOutcome.ploCode);

                        List<LecturerCurrentSubject> lecturerCurrentSubjectList = lecturerService.getLecturerSubjectsStateList(lecturerId);
                        List<LecturerSubjectsStateViewModel> subjectsStateViewModels = LecturerSubjectsStateViewModel.to(lecturerCurrentSubjectList);
                        List<String> unlinkedCloList = courseInformationService.getUnlinkedCloList(courseId);

                        Messages messages = messagesApi.preferred(request);
                        return ok(views.html.courseInformationEditForm.render(lecturerId, optionalUsername.get(), form, unlinkedPloList,
                                courseLearningOutcome, selectedPlo, courseId, subjectsStateViewModels, unlinkedCloList, request, messages));
                    }
                    else {
                        courseInformationService.updateCloToPloMap(cloTitle, ploCode, cloToPloMapId);
                        return redirect(routes.CourseInformationController.showCourseInformationDetails(lecturerId, courseId));
                    }
                }
            }
        }
        return unauthorized("You are unauthorized to access this page!");
    }

    public Result showDepartmentsBySchool(Http.Request request, Long schoolId) {
        List<Department> departments = courseInformationService.getDepartmentListBySchool(schoolId);
        List<DepartmentDto> departmentDtos = departments.stream().map(DepartmentDto::to).collect(Collectors.toList());

        return ok(Json.toJson(departmentDtos));
    }

    public Result showCoursesByProgramme(Http.Request request, Long schoolId, Long departmentId, String programme) {
        List<CourseInformation> courseInformation = courseInformationService.getCourseInformationByDept(programme);
        List<CourseInformationDto> dtos = courseInformation.stream().map(CourseInformationDto::to).collect(Collectors.toList());

        return ok(Json.toJson(dtos));
    }

    public Result showProgrammesByDept(Http.Request request, Long schoolId, Long departmentId) {
        List<String> programmes = courseInformationService.getProgrammesByDepartment(departmentId);
        List<ProgrammeDto> dtos = programmes.stream().map(ProgrammeDto::to).collect(Collectors.toList());
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
                logger.debug("plo List : " + programmeLearningOutcomeList.size());

                List<AssessmentInfo> assessmentInfos = courseInformationService.getAssessmentInfoList(lecturerId, courseId);
                logger.debug("assessment infos : " + assessmentInfos.size());

                List<CourseLearningOutcome> courseLearningOutcomes = courseInformationService.getCourseLearningOutcomeList(courseId, lecturerId);
                logger.debug("clo list : " + courseLearningOutcomes.size());

                List<CloWithTotalWeightage> cloWithTotalWeightageList = courseInformationService.getCloWithTotalWeights(lecturerId, courseId);
                Map<String, Double> cloWithTotalWeightMap = new HashMap<>();
                Map<String, Double> ploWithTotalWeightMap = new HashMap<>();
                for(int i=1; i<=12; i++) {
                    if(i<=8) {
                        cloWithTotalWeightMap.put("CLO"+i, 0.0);
                    }
                    ploWithTotalWeightMap.put("PLO"+i, 0.0);
                }

                for(CloWithTotalWeightage clo: cloWithTotalWeightageList) {
                    cloWithTotalWeightMap.replace(clo.cloCode, clo.totalWeightage);
                    ploWithTotalWeightMap.replace(clo.ploCode, clo.totalWeightage);
                }

                Map<String, List<AssessmentInfo>> assessmentInfoMap = new LinkedHashMap<>();
                for(AssessmentInfo info: assessmentInfos) {
                    if(assessmentInfoMap.containsKey(info.assessmentType)) {
                        List<AssessmentInfo> list = assessmentInfoMap.get(info.assessmentType);
                        list.add(info);
                        assessmentInfoMap.replace(info.assessmentType, list);
                    }
                    else {
                        List<AssessmentInfo> list = new ArrayList<>();
                        list.add(info);
                        assessmentInfoMap.put(info.assessmentType, list);
                    }
                }

                Double totalAssessmentWeights = courseInformationService.getTotalAssessmentWeights(lecturerId, courseId);

                CoursePlanViewModel viewModel = CoursePlanViewModel.build(courseInformation, lecturer, programmeLearningOutcomeList,
                        courseLearningOutcomes);

                Form<AssessmentInfoFormData> form = formFactory.form(AssessmentInfoFormData.class);
                lecturerService.saveLecturerCurrentSubjectState(lecturerId, courseId, courseInformation.courseName, false,
                        "Assessment");
                List<LecturerCurrentSubject> lecturerCurrentSubjectList = lecturerService.getLecturerSubjectsStateList(lecturerId);
                List<LecturerSubjectsStateViewModel> subjectsStateViewModels = LecturerSubjectsStateViewModel.to(lecturerCurrentSubjectList);

                EssentialFieldsViewModel essentialFieldsViewModel = new EssentialFieldsViewModel(lecturerId, optionalUsername.get(),
                        courseInformation.programme, courseInformation.courseName);

                Messages messages = messagesApi.preferred(request);
                return ok(views.html.assessmentInfoForm.render(essentialFieldsViewModel, viewModel, form, assessmentInfoMap,
                        totalAssessmentWeights, cloWithTotalWeightMap, ploWithTotalWeightMap, subjectsStateViewModels, request, messages));
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
                    Double totalAssessmentWeights = courseInformationService.getTotalAssessmentWeights(lecturerId, courseId);

                    Double totalWeightageByAssessment = courseInformationService.getTotalWeightageByAssessmentType(lecturerId,
                            courseId, formData.getAssessmentType());

                    boolean isWeightageExceed = courseInformationService.isWeightageExceed(formData.getAssessmentType(),
                            formData.getWeightage());

                    boolean isWeightageExceedByTotal;
                    if(formData.getAssessmentType().equals("Final Exam")) {
                        isWeightageExceedByTotal = courseInformationService.isWeightageExceedByFinalExam(lecturerId, courseId, Optional.empty(), formData.getWeightage());
                    }
                    else {
                        isWeightageExceedByTotal = courseInformationService.isWeightageExceedByCourseWork(lecturerId, courseId, Optional.empty(), formData.getWeightage());
                        logger.debug("is weightage exceed by total : " + isWeightageExceedByTotal + " _ assessment type _ " + formData.getAssessmentType());
                    }


                    if(totalAssessmentWeights+formData.getWeightage() > 100 || isWeightageExceed || isWeightageExceedByTotal) {
                        Form<AssessmentInfoFormData> formWithError;
                        if(totalAssessmentWeights+formData.getWeightage() > 100) {
                            Double require = 100 - totalAssessmentWeights;
                            formWithError = formFactory.form(AssessmentInfoFormData.class)
                                    .withGlobalError("Total weight of the assessments must be 100%. Only " + require + "% is required.");
                        }
                        else if(isWeightageExceed) {
                            Double defaultWeightage = courseInformationService.getDefaultAssessmentWeightage(formData.getAssessmentType());
                            Double require = defaultWeightage - totalWeightageByAssessment;
                            formWithError = formFactory.form(AssessmentInfoFormData.class)
                                    .withGlobalError("Total weightage of " + formData.getAssessmentType() + " should be " + defaultWeightage + "%. Only " + require + "% is required.");
                        }
                        else {
                            formWithError = formFactory.form(AssessmentInfoFormData.class)
                                    .withGlobalError("Total weightage of coursework and final exam assessments must be 50% each. Please try again.");
                        }

                        CourseInformation courseInformation = courseInformationService.getCourseInformationById(courseId);
                        List<ProgrammeLearningOutcome> programmeLearningOutcomeList = courseInformationService.getProgrammeLearningOutcomeList();
                        logger.debug("plo List : " + programmeLearningOutcomeList.size());

                        List<AssessmentInfo> assessmentInfos = courseInformationService.getAssessmentInfoList(lecturerId, courseId);
                        logger.debug("assessment infos : " + assessmentInfos.size());

                        Map<String, List<AssessmentInfo>> assessmentInfoMap = new LinkedHashMap<>();
                        for(AssessmentInfo info: assessmentInfos) {
                            if(assessmentInfoMap.containsKey(info.assessmentType)) {
                                List<AssessmentInfo> list = assessmentInfoMap.get(info.assessmentType);
                                list.add(info);
                                assessmentInfoMap.replace(info.assessmentType, list);
                            }
                            else {
                                List<AssessmentInfo> list = new ArrayList<>();
                                list.add(info);
                                assessmentInfoMap.put(info.assessmentType, list);
                            }
                        }

                        List<CourseLearningOutcome> courseLearningOutcomes = courseInformationService.getCourseLearningOutcomeList(courseId, lecturerId);
                        logger.debug("clo list : " + courseLearningOutcomes.size());

                        List<CloWithTotalWeightage> cloWithTotalWeightageList = courseInformationService.getCloWithTotalWeights(lecturerId, courseId);
                        Map<String, Double> cloWithTotalWeightMap = new HashMap<>();
                        Map<String, Double> ploWithTotalWeightMap = new HashMap<>();
                        for(int i=1; i<=12; i++) {
                            if(i<=8) {
                                cloWithTotalWeightMap.put("CLO"+i, 0.0);
                            }
                            ploWithTotalWeightMap.put("PLO"+i, 0.0);
                        }

                        for(CloWithTotalWeightage clo: cloWithTotalWeightageList) {
                            cloWithTotalWeightMap.replace(clo.cloCode, clo.totalWeightage);
                            ploWithTotalWeightMap.replace(clo.ploCode, clo.totalWeightage);
                        }

                        Lecturer lecturer = lecturerService.getLecturerById(lecturerId);
                        CoursePlanViewModel viewModel = CoursePlanViewModel.build(courseInformation, lecturer, programmeLearningOutcomeList,
                                courseLearningOutcomes);

                        List<LecturerCurrentSubject> lecturerCurrentSubjectList = lecturerService.getLecturerSubjectsStateList(lecturerId);
                        List<LecturerSubjectsStateViewModel> subjectsStateViewModels = LecturerSubjectsStateViewModel.to(lecturerCurrentSubjectList);

                        EssentialFieldsViewModel essentialFieldsViewModel = new EssentialFieldsViewModel(lecturerId, optionalUsername.get(),
                                courseInformation.programme, courseInformation.courseName);

                        Messages messages = messagesApi.preferred(request);

                        return ok(views.html.assessmentInfoForm.render(essentialFieldsViewModel, viewModel,
                                formWithError, assessmentInfoMap, totalAssessmentWeights, cloWithTotalWeightMap,
                                ploWithTotalWeightMap, subjectsStateViewModels, request, messages));

                    }
                    else {
                        courseInformationService.saveAssessmentInfo(formData.getAssessment(), formData.getAssessmentType(),
                                formData.getFullMarks(), formData.getWeightage(), formData.getCloTitle(), lecturerId,
                                courseId);

                        return redirect(routes.CourseInformationController.showAssessmentInformationForm(lecturerId, courseId));
                    }
                }
            }
        }
        return unauthorized("You are unauthorized to access this page!");
    }

    public Result deleteAssessment(Http.Request request, Long lecturerId, Long courseId, Long assessmentId) {
        Optional<String> optionalSessionIdString = request.session().get("id");
        Optional<String> optionalUsername = request.session().get("username");

        if(optionalSessionIdString.isPresent() && optionalUsername.isPresent()) {
            Long sessionId = Long.parseLong(optionalSessionIdString.get());
            if (sessionId == lecturerId) {
                courseInformationService.deleteAssessment(assessmentId);
                return redirect(routes.CourseInformationController.showAssessmentInformationForm(lecturerId, courseId));
            }
        }

        return unauthorized("You are unauthorized to access this page!");
    }

    public Result deleteAllAssessments(Http.Request request, Long lecturerId, Long courseId) {
        Optional<String> optionalSessionIdString = request.session().get("id");
        Optional<String> optionalUsername = request.session().get("username");

        if(optionalSessionIdString.isPresent() && optionalUsername.isPresent()) {
            Long sessionId = Long.parseLong(optionalSessionIdString.get());
            if (sessionId == lecturerId) {
                courseInformationService.deleteAllAssessments(lecturerId, courseId);
                return redirect(routes.CourseInformationController.showAssessmentInformationForm(lecturerId, courseId));
            }
        }

        return unauthorized("You are unauthorized to access this page!");
    }

    public Result showEditAssessmentForm(Http.Request request, Long lecturerId, Long courseId, Long assessmentId) {
        Optional<String> optionalSessionIdString = request.session().get("id");
        Optional<String> optionalUsername = request.session().get("username");

        if(optionalSessionIdString.isPresent() && optionalUsername.isPresent()) {
            Long sessionId = Long.parseLong(optionalSessionIdString.get());
            if (sessionId == lecturerId) {
                Form<AssessmentInfoFormData> form = formFactory.form(AssessmentInfoFormData.class);
                CourseInformation courseInformation = courseInformationService.getCourseInformationById(courseId);
                List<CourseLearningOutcome> courseLearningOutcomes = courseInformationService.getCourseLearningOutcomeList(courseId, lecturerId);
                AssessmentInfo assessmentInfo = courseInformationService.getAssessmentInfo(assessmentId);
                List<LecturerCurrentSubject> lecturerCurrentSubjectList = lecturerService.getLecturerSubjectsStateList(lecturerId);
                List<LecturerSubjectsStateViewModel> subjectsStateViewModels = LecturerSubjectsStateViewModel.to(lecturerCurrentSubjectList);

                EssentialFieldsViewModel essentialFieldsViewModel = new EssentialFieldsViewModel(lecturerId, optionalUsername.get(),
                        courseInformation.programme, courseInformation.courseName);

                return ok(views.html.assessmentInfoEditForm.render(essentialFieldsViewModel, form, assessmentInfo,
                        courseInformation, courseLearningOutcomes, subjectsStateViewModels));
            }
        }

        return unauthorized("You are unauthorized to access this page!");
    }

    public Result handleEditAssessmentForm(Http.Request request, Long lecturerId, Long courseId, Long assessmentId) {
        Optional<String> optionalSessionIdString = request.session().get("id");
        Optional<String> optionalUsername = request.session().get("username");

        if(optionalSessionIdString.isPresent() && optionalUsername.isPresent()) {
            Long sessionId = Long.parseLong(optionalSessionIdString.get());
            if (sessionId == lecturerId) {
                Form<AssessmentInfoFormData> fromRequest = formFactory.form(AssessmentInfoFormData.class).bindFromRequest(request);
                if(fromRequest.hasGlobalErrors()) {
                    return redirect(routes.CourseInformationController.showEditAssessmentForm(lecturerId, courseId, assessmentId));
                }
                else {
                    AssessmentInfoFormData formData = fromRequest.get();
                    AssessmentInfo assessmentInfo = courseInformationService.getAssessmentInfo(assessmentId);
                    Double totalWeightageByAssessment = courseInformationService.getTotalWeightageByAssessmentType(lecturerId,
                            courseId, formData.getAssessmentType()) - assessmentInfo.weightage;

                    boolean isWeightageExceed = courseInformationService.isWeightageExceed(formData.getAssessmentType(),
                            formData.getWeightage());

                    boolean isWeightageExceedByTotal;
                    if(formData.getAssessmentType().equals("Final Exam")) {
                        isWeightageExceedByTotal = courseInformationService.isWeightageExceedByFinalExam(lecturerId, courseId,
                                Optional.of(assessmentInfo), formData.getWeightage());
                    }
                    else {
                        isWeightageExceedByTotal = courseInformationService.isWeightageExceedByCourseWork(lecturerId, courseId,
                                Optional.of(assessmentInfo), formData.getWeightage());
                    }

                    logger.debug("is weightage exceed : " + isWeightageExceed);
                    logger.debug("is weightage exceed by total : " + isWeightageExceedByTotal);
                    if(isWeightageExceed || isWeightageExceedByTotal) {
                        Form<AssessmentInfoFormData> formDataForm;

                        if(isWeightageExceed) {
                            Double require = totalWeightageByAssessment - formData.getWeightage();
                            Double defaultWeightage = courseInformationService.getDefaultAssessmentWeightage(formData.getAssessmentType());
                            formDataForm = formFactory.form(AssessmentInfoFormData.class)
                                    .withGlobalError("Total weightage of " + formData.getAssessmentType() + " should be" + defaultWeightage + "%. Only " + require + "% is required.");
                        }
                        else {
                            formDataForm = formFactory.form(AssessmentInfoFormData.class)
                                    .withGlobalError("Total weightage of coursework and final exam assessments must be 50% each. Please try again.");
                        }

                        CourseInformation courseInformation = courseInformationService.getCourseInformationById(courseId);
                        List<CourseLearningOutcome> courseLearningOutcomes = courseInformationService.getCourseLearningOutcomeList(courseId, lecturerId);
                        List<LecturerCurrentSubject> lecturerCurrentSubjectList = lecturerService.getLecturerSubjectsStateList(lecturerId);
                        List<LecturerSubjectsStateViewModel> subjectsStateViewModels = LecturerSubjectsStateViewModel.to(lecturerCurrentSubjectList);

                        EssentialFieldsViewModel essentialFieldsViewModel = new EssentialFieldsViewModel(lecturerId, optionalUsername.get(),
                                courseInformation.programme, courseInformation.courseName);

                        return ok(views.html.assessmentInfoEditForm.render(essentialFieldsViewModel, formDataForm, assessmentInfo,
                                courseInformation, courseLearningOutcomes, subjectsStateViewModels));
                    }
                    else {
                        assessmentInfo.assessment = formData.getAssessment();
                        assessmentInfo.assessmentType = formData.getAssessmentType();
                        assessmentInfo.fullMarks = formData.getFullMarks();
                        assessmentInfo.weightage = formData.getWeightage();
                        assessmentInfo.cloCode = formData.getCloTitle();
                        courseInformationService.updateAssessmentInfo(assessmentInfo);

                        return redirect(routes.CourseInformationController.showAssessmentInformationForm(lecturerId, courseId));
                    }
                }
            }
        }
        return unauthorized("You are unauthorized to access this page!");
    }

    public Result showStudentList(Http.Request request, Long lecturerId) {
        Optional<String> optionalSessionIdString = request.session().get("id");
        Optional<String> optionalUsername = request.session().get("username");

        if(optionalSessionIdString.isPresent() && optionalUsername.isPresent()) {
            Long sessionId = Long.parseLong(optionalSessionIdString.get());
            if (sessionId == lecturerId) {
                List<Student> studentList = studentService.getStudentList(lecturerId);
                List<LecturerCurrentSubject> lecturerCurrentSubjectList = lecturerService.getLecturerSubjectsStateList(lecturerId);
                List<LecturerSubjectsStateViewModel> subjectsStateViewModels = LecturerSubjectsStateViewModel.to(lecturerCurrentSubjectList);

                Form<StudentInfoFormData> form = formFactory.form(StudentInfoFormData.class);
                Messages messages = messagesApi.preferred(request);

                return ok(views.html.studentList.render(lecturerId, optionalUsername.get(), studentList, form,
                        subjectsStateViewModels, request, messages));
            }
        }
        return unauthorized("You are unauthorized to access this page!");
    }

    public Result addNewStudent(Http.Request request, Long lecturerId) {
        Optional<String> optionalSessionIdString = request.session().get("id");
        Optional<String> optionalUsername = request.session().get("username");

        if (optionalSessionIdString.isPresent() && optionalUsername.isPresent()) {
            Long sessionId = Long.parseLong(optionalSessionIdString.get());
            if (sessionId == lecturerId) {
                Form<StudentInfoFormData> fromRequest = formFactory.form(StudentInfoFormData.class).bindFromRequest(request);
                if (fromRequest.hasGlobalErrors()) {
                    return redirect(routes.CourseInformationController.showStudentList(lecturerId));
                } else {
                    StudentInfoFormData formData = fromRequest.get();
                    int countIdDuplicate = studentService.hasDuplicateCodeNumber(formData.getCodeNumber(), lecturerId);
                    int countNameDuplicate = studentService.hasDuplicateName(formData.getFirstName(), formData.getLastName(),
                            lecturerId);
                    int countEmailDuplicate = studentService.hasDuplicateEmail(formData.getEmail(), lecturerId);

                    boolean hasDuplicate = countIdDuplicate > 0 || countNameDuplicate > 0 || countEmailDuplicate > 0;

                    if (!hasDuplicate) {
                        studentService.saveStudent(formData.getCodeNumber(), formData.getFirstName(),
                                formData.getLastName(), formData.getGender(), formData.getCurrentSemester(), formData.getEmail(), lecturerId);

                        return redirect(routes.CourseInformationController.showStudentList(lecturerId));
                    } else {
                        Form<StudentInfoFormData> form;
                        if (countIdDuplicate > 0) {
                            form = formFactory.form(StudentInfoFormData.class)
                                    .withGlobalError("Student with ID Number - " + formData.getCodeNumber() + " is already exists. Please try again.");
                        } else if (countEmailDuplicate > 0) {
                            form = formFactory.form(StudentInfoFormData.class)
                                    .withGlobalError("Student with Email - " + formData.getEmail() + " is already exists. Please try again.");
                        } else {
                            form = formFactory.form(StudentInfoFormData.class)
                                    .withGlobalError("Student with Name - " + formData.getFirstName() + " " +
                                            formData.getLastName() + " is already exists. Please try again.");

                            List<Student> studentList = studentService.getStudentList(lecturerId);
                            List<LecturerCurrentSubject> lecturerCurrentSubjectList = lecturerService.getLecturerSubjectsStateList(lecturerId);
                            List<LecturerSubjectsStateViewModel> subjectsStateViewModels = LecturerSubjectsStateViewModel.to(lecturerCurrentSubjectList);

                            Messages messages = messagesApi.preferred(request);

                            return ok(views.html.studentList.render(lecturerId, optionalUsername.get(), studentList, form,
                                    subjectsStateViewModels, request, messages));
                        }

                    }
                }
            }
        }
        return unauthorized("You are unauthorized to access this page!");
    }

    public Result deleteStudentInformation(Http.Request request, Long lecturerId, Long studentId) {
        Optional<String> optionalSessionIdString = request.session().get("id");
        Optional<String> optionalUsername = request.session().get("username");

        if(optionalSessionIdString.isPresent() && optionalUsername.isPresent()) {
            Long sessionId = Long.parseLong(optionalSessionIdString.get());
            if (sessionId == lecturerId) {
                studentService.deleteStudent(studentId);
                return redirect(routes.CourseInformationController.showStudentList(lecturerId));
            }
        }

        return unauthorized("You are unauthorized to access this page!");
    }


    public Result showStudentMarksEntryForm(Http.Request request, Long lecturerId, Long courseId) {
        Optional<String> optionalSessionIdString = request.session().get("id");
        Optional<String> optionalUsername = request.session().get("username");

        if(optionalSessionIdString.isPresent() && optionalUsername.isPresent()) {
            Long sessionId = Long.parseLong(optionalSessionIdString.get());
            if (sessionId == lecturerId) {
                List<Student> studentList = studentService.getStudentListByLecturerAndCourse(lecturerId, courseId);
                List<AssessmentInfo> assessmentInfos = courseInformationService.getAssessmentInfoList(lecturerId, courseId);
                LinkedHashMap<String, List<AssessmentInfo>> assessmentMap = new LinkedHashMap<>();
                List<StudentMarksViewModel.AssessmentOrder> assessmentOrders = new ArrayList<>();
                int order = 0;
                for(AssessmentInfo info: assessmentInfos) {
                    if(assessmentMap.containsKey(info.assessmentType)) {
                        List<AssessmentInfo> infoList = assessmentMap.get(info.assessmentType);
                        infoList.add(info);
                        assessmentMap.replace(info.assessmentType, infoList);
                    }
                    else {
                        List<AssessmentInfo> infoList = new ArrayList<>();
                        infoList.add(info);
                        assessmentMap.put(info.assessmentType, infoList);
                        assessmentOrders.add(new StudentMarksViewModel.AssessmentOrder(order++, info.assessmentType));
                    }
                    logger.debug("Assessment type : " + info.assessmentType + " Assessment Id : " + info.id);
                }

                int assessmentColSize = 0;
                for (List<AssessmentInfo> value : assessmentMap.values()) {
                    assessmentColSize += value.size();
                }

                List<StudentMarksViewModel> studentMarksViewModels = new ArrayList<>();
                for(Student student: studentList) {
                    List<StudentMarks> studentMarks = studentService.getStudentMarksList(student.id, lecturerId, courseId);
                    studentMarksViewModels.add(StudentMarksViewModel.build(student, studentMarks));
                }

                CourseInformation courseInformation = courseInformationService.getCourseInformationById(courseId);

                if(studentMarksViewModels.size() > 0) {
                    logger.debug("Completed : true");
                    lecturerService.saveLecturerCurrentSubjectState(lecturerId, courseId, courseInformation.courseName, true,
                            "Student");
                }
                else {
                    logger.debug("Completed : false");
                    lecturerService.saveLecturerCurrentSubjectState(lecturerId, courseId, courseInformation.courseName, false,
                            "Student");
                }

                List<LecturerCurrentSubject> lecturerCurrentSubjectList = lecturerService.getLecturerSubjectsStateList(lecturerId);
                List<LecturerSubjectsStateViewModel> subjectsStateViewModels = LecturerSubjectsStateViewModel.to(lecturerCurrentSubjectList);

                logger.debug("Student list : " + studentList.size());

                EssentialFieldsViewModel essentialFieldsViewModel = new EssentialFieldsViewModel(lecturerId, optionalUsername.get(),
                        courseInformation.programme, courseInformation.courseName);

                Messages messages = messagesApi.preferred(request);
                List<Student> unmappedStudentList = studentService.getUnmappedStudentList(lecturerId, courseId);

                return ok(views.html.studentMarksEntryForm.render(essentialFieldsViewModel, courseInformation, studentList,
                        assessmentMap, studentMarksViewModels, assessmentOrders, subjectsStateViewModels, unmappedStudentList,
                        assessmentColSize, request, messages));
            }
        }

        return unauthorized("You are unauthorized to access this page!");
    }

    public Result handleStudentMarksEntryForm(Http.Request request, Long lecturerId, Long courseId) {
        Optional<String> optionalSessionIdString = request.session().get("id");
        Optional<String> optionalUsername = request.session().get("username");

        if(optionalSessionIdString.isPresent() && optionalUsername.isPresent()) {
            Long sessionId = Long.parseLong(optionalSessionIdString.get());
            if (sessionId == lecturerId) {
                DynamicForm requestData = formFactory.form().bindFromRequest(request);
                Long studentId = Long.parseLong(requestData.get("studentId").trim());
                CourseInformation courseInformation = courseInformationService.getCourseInformationById(courseId);
                List<AssessmentInfo> assessmentInfos = courseInformationService.getAssessmentInfoList(lecturerId, courseId);

                studentService.saveStudentToCourseInformationMap(lecturerId, studentId, courseInformation, assessmentInfos);
                return redirect(routes.CourseInformationController.showStudentMarksEntryForm(lecturerId, courseId));
            }
        }

        return unauthorized("You are unauthorized to access this page!");
    }

    public Result removeStudentFromCourse(Http.Request request, Long lecturerId, Long courseId, Long studentId) {
        Optional<String> optionalSessionIdString = request.session().get("id");
        Optional<String> optionalUsername = request.session().get("username");

        if(optionalSessionIdString.isPresent() && optionalUsername.isPresent()) {
            Long sessionId = Long.parseLong(optionalSessionIdString.get());
            if (sessionId == lecturerId) {
                studentService.deleteStudentCourseMap(studentId, courseId);
                return redirect(routes.CourseInformationController.showStudentMarksEntryForm(lecturerId, courseId));
            }
        }

        return unauthorized("You are unauthorized to access this page!");
    }


    public Result showEditStudentForm(Http.Request request, Long lecturerId, Long studentId) {
        Optional<String> optionalSessionIdString = request.session().get("id");
        Optional<String> optionalUsername = request.session().get("username");

        if(optionalSessionIdString.isPresent() && optionalUsername.isPresent()) {
            Long sessionId = Long.parseLong(optionalSessionIdString.get());
            if (sessionId == lecturerId) {
                Form<StudentInfoFormData> form = formFactory.form(StudentInfoFormData.class);
                Student student = studentService.getStudent(studentId);
                List<LecturerCurrentSubject> lecturerCurrentSubjectList = lecturerService.getLecturerSubjectsStateList(lecturerId);
                List<LecturerSubjectsStateViewModel> subjectsStateViewModels = LecturerSubjectsStateViewModel.to(lecturerCurrentSubjectList);

                Messages messages = messagesApi.preferred(request);
                return ok(views.html.studentInfoEditForm.render(lecturerId, optionalUsername.get(), form, student,
                        subjectsStateViewModels, request, messages));
            }
        }

        return unauthorized("You are unauthorized to access this page!");
    }

    public Result handleEditStudentForm(Http.Request request, Long lecturerId, Long studentId) {
        Optional<String> optionalSessionIdString = request.session().get("id");
        Optional<String> optionalUsername = request.session().get("username");

        if(optionalSessionIdString.isPresent() && optionalUsername.isPresent()) {
            Long sessionId = Long.parseLong(optionalSessionIdString.get());
            if (sessionId == lecturerId) {
                Form<StudentInfoFormData> fromRequest = formFactory.form(StudentInfoFormData.class).bindFromRequest(request);
                if(fromRequest.hasGlobalErrors()) {
                    return redirect(routes.CourseInformationController.showEditStudentForm(lecturerId, studentId));
                }
                else {
                    StudentInfoFormData formData = fromRequest.get();
                    Student student = studentService.getStudent(studentId);
                    int countIdDuplicate = studentService.hasDuplicateCodeNumber(formData.getCodeNumber(), lecturerId);
                    int countNameDuplicate = studentService.hasDuplicateName(formData.getFirstName(), formData.getLastName(),
                            lecturerId);
                    int countEmailDuplicate = studentService.hasDuplicateEmail(formData.getEmail(), lecturerId);

                    boolean hasDuplicate = (countIdDuplicate > 0 && !student.codeNumber.equals(formData.getCodeNumber()))
                            || (countNameDuplicate > 0 && !student.firstName.equals(formData.getFirstName())
                            && !student.lastName.equals(formData.getLastName()) )
                            || (countEmailDuplicate > 0 && !student.email.equals(formData.getEmail()));

                    if(hasDuplicate) {
                        Form<StudentInfoFormData> form;

                        if(countIdDuplicate > 0 && !student.codeNumber.equals(formData.getCodeNumber())) {
                            form = formFactory.form(StudentInfoFormData.class)
                                    .withGlobalError("Student with ID Number - " + formData.getCodeNumber() + " is already exists. Please try again.");
                        }
                        else if(countNameDuplicate > 0 && !student.firstName.equals(formData.getFirstName())
                                && !student.lastName.equals(formData.getLastName())) {
                            form = formFactory.form(StudentInfoFormData.class)
                                    .withGlobalError("Student with Name - " + formData.getFirstName() + " "
                                            + formData.getLastName() + " is already exists. Please try again.");
                        }
                        else {
                            form = formFactory.form(StudentInfoFormData.class)
                                    .withGlobalError("Student with Email - " + formData.getEmail() + " is already exists. Please try again.");
                        }

                        List<LecturerCurrentSubject> lecturerCurrentSubjectList = lecturerService.getLecturerSubjectsStateList(lecturerId);
                        List<LecturerSubjectsStateViewModel> subjectsStateViewModels = LecturerSubjectsStateViewModel.to(lecturerCurrentSubjectList);

                        Messages messages = messagesApi.preferred(request);
                        return ok(views.html.studentInfoEditForm.render(lecturerId, optionalUsername.get(), fromRequest, student,
                                subjectsStateViewModels, request, messages));
                    }
                    else {
                        student.firstName = formData.getFirstName();
                        student.lastName = formData.getLastName();
                        student.codeNumber = formData.getCodeNumber();
                        //student.currentSemester = formData.getCurrentSemester();
                        student.email = formData.getEmail();
                        student.gender = formData.getGender();
                        studentService.updateStudent(student);

                        return redirect(routes.CourseInformationController.showStudentList(lecturerId));
                    }
                }
            }
        }
        return unauthorized("You are unauthorized to access this page!");
    }

    public Result saveStudentMarksForAssessment(Http.Request request, Long lecturerId, Long courseId, Long assessmentId,
                                                Long studentId, Long marksEntryId) {
        Optional<String> optionalSessionIdString = request.session().get("id");
        Optional<String> optionalUsername = request.session().get("username");

        if(optionalSessionIdString.isPresent() && optionalUsername.isPresent()) {
            Long sessionId = Long.parseLong(optionalSessionIdString.get());
            if (sessionId == lecturerId) {
                DynamicForm requestData = formFactory.form().bindFromRequest(request);
                Double marks = Double.parseDouble(requestData.get("marks").trim());
                logger.debug("Marks : " + marks);
                studentService.updateStudentMarks(marksEntryId, marks);
                //return redirect(routes.CourseInformationController.showStudentInformationForm(lecturerId));
                return ok();
            }
        }

        return unauthorized("You are unauthorized to access this page!");
    }

    public Result showReports(Http.Request request, Long lecturerId, Long courseId) {
        Optional<String> optionalSessionIdString = request.session().get("id");
        Optional<String> optionalUsername = request.session().get("username");

        if(optionalSessionIdString.isPresent() && optionalUsername.isPresent()) {
            Long sessionId = Long.parseLong(optionalSessionIdString.get());
            if (sessionId == lecturerId) {
                Messages messages = messagesApi.preferred(request);
                CourseInformation courseInformation = courseInformationService.getCourseInformationById(courseId);
                List<Student> studentList = studentService.getStudentListByLecturerAndCourse(lecturerId, courseId);
                List<StatisticsReport> statisticsReports = studentService.getStatisticsMarks(lecturerId, courseId);
                List<StudentStatisticsReport> studentStatisticsReports = studentService.getStudentStatisticsReport(lecturerId, courseId);

                logger.debug("statisticsReports : " + statisticsReports.size());
                logger.debug("Student statistic : " + studentStatisticsReports.size());
                Either<GradeViewModel.ErrorType, GradeViewModel> either = GradeViewModel.build(courseInformation, studentList,
                        statisticsReports, studentStatisticsReports);

                return either.fold(
                        error -> ok(error.toString()),
                        gradeViewModel -> {
                            List<CloWithTotalWeightage> cloWithTotalWeightageList = courseInformationService.getCloWithTotalWeights(lecturerId, courseId);
                            List<CloAttainmentReport> cloAttainments = studentService.getCloAttainments(lecturerId, courseId);
                            List<PreviousCloRecord> previousCloRecords = courseInformationService.getPreviousCloRecordList(lecturerId, courseId);
                            CloViewModel cloViewModel = CloViewModel.build(cloWithTotalWeightageList, cloAttainments, studentList.size(), previousCloRecords);

                            lecturerService.saveLecturerCurrentSubjectState(lecturerId, courseId, courseInformation.courseName, true,
                                    "Report");
                            List<LecturerCurrentSubject> lecturerCurrentSubjectList = lecturerService.getLecturerSubjectsStateList(lecturerId);
                            List<LecturerSubjectsStateViewModel> subjectsStateViewModels = LecturerSubjectsStateViewModel.to(lecturerCurrentSubjectList);

                            EssentialFieldsViewModel essentialFieldsViewModel = new EssentialFieldsViewModel(lecturerId, optionalUsername.get(),
                                    courseInformation.programme, courseInformation.courseName);

                            return ok(views.html.reports.render(essentialFieldsViewModel, gradeViewModel, cloViewModel,
                                    subjectsStateViewModels, request, messages));
                        }
                );

            }
        }

        return unauthorized("You are unauthorized to access this page!");
    }

    public Result saveStudentAttemptNumber(Http.Request request, Long lecturerId, Long courseId, Long studentId) {
        Optional<String> optionalSessionIdString = request.session().get("id");
        Optional<String> optionalUsername = request.session().get("username");

        if(optionalSessionIdString.isPresent() && optionalUsername.isPresent()) {
            Long sessionId = Long.parseLong(optionalSessionIdString.get());
            if (sessionId == lecturerId) {
                DynamicForm requestData = formFactory.form().bindFromRequest(request);
                int numberOfAttempt = Integer.parseInt(requestData.get("numberOfAttempt").trim());
                logger.debug("numberOfAttempt : " + numberOfAttempt);
                studentService.saveStudentAttemptNumber(lecturerId, courseId, studentId, numberOfAttempt);
                return redirect(routes.CourseInformationController.showReports(lecturerId, courseId));
            }
        }

        return unauthorized("You are unauthorized to access this page!");
    }

    public Result calculateGradeDistribution(Http.Request request, Long lecturerId, Long courseId) {
        CourseInformation courseInformation = courseInformationService.getCourseInformationById(courseId);
        List<Student> studentList = studentService.getStudentListByLecturerAndCourse(lecturerId, courseId);
        List<StatisticsReport> statisticsReports = studentService.getStatisticsMarks(lecturerId, courseId);
        List<StudentStatisticsReport> studentStatisticsReports = studentService.getStudentStatisticsReport(lecturerId, courseId);
        Either<GradeViewModel.ErrorType, GradeViewModel> either = GradeViewModel.build(courseInformation, studentList, statisticsReports, studentStatisticsReports);

        return either.fold(
                error -> ok(error.toString()),
                gradeViewModel -> {
                    List<GradeDistributionDto> dtos = GradeDistributionDto.to(gradeViewModel);
                    return ok(Json.toJson(dtos));
                }
        );
    }

    public Result generateClassCloAttainment(Http.Request request, Long lecturerId, Long courseId) {
        List<Student> studentList = studentService.getStudentListByLecturerAndCourse(lecturerId, courseId);
        List<CloWithTotalWeightage> cloWithTotalWeightageList = courseInformationService.getCloWithTotalWeights(lecturerId, courseId);
        List<CloAttainmentReport> cloAttainments = studentService.getCloAttainments(lecturerId, courseId);
        List<PreviousCloRecord> previousCloRecords = courseInformationService.getPreviousCloRecordList(lecturerId, courseId);
        CloViewModel cloViewModel = CloViewModel.build(cloWithTotalWeightageList, cloAttainments, studentList.size(), previousCloRecords);

        return ok(Json.toJson(cloViewModel.cloAnalysisList));
    }

    public Result generateClassPloAttainment(Http.Request request, Long lecturerId, Long courseId) {
        List<Student> studentList = studentService.getStudentListByLecturerAndCourse(lecturerId, courseId);
        List<CloWithTotalWeightage> cloWithTotalWeightageList = courseInformationService.getCloWithTotalWeights(lecturerId, courseId);
        List<CloAttainmentReport> cloAttainments = studentService.getCloAttainments(lecturerId, courseId);
        List<PreviousCloRecord> previousCloRecords = courseInformationService.getPreviousCloRecordList(lecturerId, courseId);
        CloViewModel cloViewModel = CloViewModel.build(cloWithTotalWeightageList, cloAttainments, studentList.size(), previousCloRecords);

        return ok(Json.toJson(cloViewModel.ploAnalysisList));
    }

    public Result saveCloPreviousSemClassAvgRecord(Http.Request request, Long lecturerId, Long courseId, String cloCode) {
        Optional<String> optionalSessionIdString = request.session().get("id");
        Optional<String> optionalUsername = request.session().get("username");

        if(optionalSessionIdString.isPresent() && optionalUsername.isPresent()) {
            Long sessionId = Long.parseLong(optionalSessionIdString.get());
            if (sessionId == lecturerId) {
                DynamicForm requestData = formFactory.form().bindFromRequest(request);
                Double previousSemClassAvg = Double.parseDouble(requestData.get("previousSemClassAvg").trim());
                logger.debug("previousSemClassAvg : " + previousSemClassAvg);
                courseInformationService.savePreviousCloRecord(lecturerId, courseId, cloCode, Optional.of(previousSemClassAvg),
                        Optional.empty());

                return redirect(routes.CourseInformationController.showReports(lecturerId, courseId));
            }
        }

        return unauthorized("You are unauthorized to access this page!");
    }

    public Result saveComment(Http.Request request, Long lecturerId, Long courseId, String cloCode) {
        Optional<String> optionalSessionIdString = request.session().get("id");
        Optional<String> optionalUsername = request.session().get("username");

        if(optionalSessionIdString.isPresent() && optionalUsername.isPresent()) {
            Long sessionId = Long.parseLong(optionalSessionIdString.get());
            if (sessionId == lecturerId) {
                DynamicForm requestData = formFactory.form().bindFromRequest(request);
                String comment = requestData.get("comment").trim();
                logger.debug("comment : " + comment);
                courseInformationService.savePreviousCloRecord(lecturerId, courseId, cloCode, Optional.empty(),
                        Optional.of(comment));

                return redirect(routes.CourseInformationController.showReports(lecturerId, courseId));
            }
        }

        return unauthorized("You are unauthorized to access this page!");
    }

    public Result showDashboard(Http.Request request, Long lecturerId) {
        Optional<String> optionalSessionIdString = request.session().get("id");
        Optional<String> optionalUsername = request.session().get("username");

        if(optionalSessionIdString.isPresent() && optionalUsername.isPresent()) {
            Long sessionId = Long.parseLong(optionalSessionIdString.get());
            if (sessionId == lecturerId) {
                String username = optionalUsername.get();
                List<LecturerCurrentSubject> lecturerCurrentSubjectList = lecturerService.getLecturerSubjectsStateList(lecturerId);
                List<LecturerSubjectsStateViewModel> subjectsStateViewModels = LecturerSubjectsStateViewModel.to(lecturerCurrentSubjectList);

                Messages messages = messagesApi.preferred(request);
                return ok(views.html.dashboard.render(lecturerId, username, subjectsStateViewModels, request, messages));
            }
        }
        return unauthorized("You are unauthorized to access this page!");
    }
}

