package controllers;

import forms.AuthFormData;
import models.Lecturer;
import models.LecturerCurrentSubject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.DynamicForm;
import play.data.Form;
import play.data.FormFactory;
import play.i18n.Messages;
import play.i18n.MessagesApi;
import play.libs.Files;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.LecturerService;
import viewModels.LecturerSubjectsStateViewModel;

import javax.inject.Inject;
import java.io.File;
import java.nio.file.Paths;
import java.util.*;

public class AuthController extends Controller {
    private final Logger logger = LoggerFactory.getLogger("application");

    private FormFactory formFactory;
    private LecturerService lecturerService;
    private MessagesApi messagesApi;

    @Inject
    public AuthController(FormFactory formFactory, LecturerService lecturerService, MessagesApi messagesApi) {
        this.formFactory = formFactory;
        this.lecturerService = lecturerService;
        this.messagesApi = messagesApi;
    }

    public Result showLoginForm(Http.Request request){
        Form<AuthFormData> form = formFactory.form(AuthFormData.class);
        return ok(views.html.login.render(form));
    }

    public Result handleLoginForm(Http.Request request) {
        DynamicForm requestData = formFactory.form().bindFromRequest(request);
        String email = requestData.get("email").trim();
        String password = requestData.get("password").trim();

        Lecturer lecturer = lecturerService.getLecturerByEmail(email);
        if(lecturer != null) {
            boolean isAuthentic = lecturerService.authenticate(email, password);
            if(isAuthentic) {
                Map<String, String> sessionValuesMap = new HashMap<>();
                sessionValuesMap.put("id", String.valueOf(lecturer.id));
                sessionValuesMap.put("username", lecturer.firstName + " " + lecturer.lastName);
                return redirect(routes.CourseInformationController.showDashboard(lecturer.id)).addingToSession(request, sessionValuesMap);
            }
        }

        Form<AuthFormData> loginFormWithError = formFactory.form(AuthFormData.class).withGlobalError("Something went wrong. Please try again.");

        return ok(views.html.login.render(loginFormWithError));
    }

    public Result logout(){
        return redirect(routes.AuthController.showLoginForm()).withNewSession();
    }

    public Result resetPassword(Http.Request request) {
        Form<AuthFormData> form = formFactory.form(AuthFormData.class);
        return ok(views.html.resetPassword.render(form));
    }

    public Result handleResetPassword(Http.Request request) {
        DynamicForm requestData = formFactory.form().bindFromRequest(request);
        String email = requestData.get("email").trim();
        String newPassword = requestData.get("password").trim();

        Lecturer lecturer = lecturerService.getLecturerByEmail(email);
        if(lecturer != null) {
            lecturerService.updatePassword(lecturer, newPassword);
            return redirect(routes.AuthController.showLoginForm());
        }

        Form<AuthFormData> loginFormWithError = formFactory.form(AuthFormData.class).withGlobalError("Invalid email address!");
        return ok(views.html.resetPassword.render(loginFormWithError));
    }

    public Result showProfile(Http.Request request, Long lecturerId) {
        Optional<String> optionalSessionIdString = request.session().get("id");
        Optional<String> optionalUsername = request.session().get("username");

        if(optionalSessionIdString.isPresent() && optionalUsername.isPresent()) {
            Long sessionId = Long.parseLong(optionalSessionIdString.get());
            if (sessionId == lecturerId) {
                List<LecturerCurrentSubject> lecturerCurrentSubjectList = lecturerService.getLecturerSubjectsStateList(lecturerId);
                List<LecturerSubjectsStateViewModel> subjectsStateViewModels = LecturerSubjectsStateViewModel.to(lecturerCurrentSubjectList);

                List<Integer> dayList = new ArrayList<>();
                for(int i=1; i<=31; i++) {
                    dayList.add(i);
                }

                List<Integer> monthList = new ArrayList<>();
                for(int i=1; i<=12; i++) {
                    monthList.add(i);
                }

                List<Integer> yearList = new ArrayList<>();
                for(int i=1940; i<=2005; i++) {
                    yearList.add(i);
                }

                Messages messages = messagesApi.preferred(request);
                Lecturer lecturer = lecturerService.getLecturerById(lecturerId);
                return ok(views.html.profile.render(lecturerId, optionalUsername.get(), lecturer.image, lecturer,
                        subjectsStateViewModels, dayList, monthList, yearList,
                        request, messages));
            }
        }
        return unauthorized("You are unauthorized to access this page!");
    }

    public Result handleProfile(Http.Request request, Long lecturerId) {
        Optional<String> optionalSessionIdString = request.session().get("id");
        Optional<String> optionalUsername = request.session().get("username");

        if(optionalSessionIdString.isPresent() && optionalUsername.isPresent()) {
            Long sessionId = Long.parseLong(optionalSessionIdString.get());
            if (sessionId == lecturerId) {
                Lecturer lecturer = lecturerService.getLecturerById(lecturerId);

                DynamicForm requestData = formFactory.form().bindFromRequest(request);
                lecturer.codeNumber = requestData.get("codeNumber").trim();
                lecturer.firstName = requestData.get("firstName").trim();
                lecturer.lastName = requestData.get("lastName").trim();
                lecturer.gender = requestData.get("gender").trim();
                lecturer.email = requestData.get("email").trim();
                lecturer.phoneNumber = requestData.get("phoneNumber").trim();
                lecturer.birthMonth = Integer.parseInt(requestData.get("month").trim());
                lecturer.birthDay = Integer.parseInt(requestData.get("day").trim());
                lecturer.birthYear = Integer.parseInt(requestData.get("year").trim());

                lecturerService.updateLecturer(lecturer);
                return redirect(routes.AuthController.showProfile(lecturerId));
            }
        }
        return unauthorized("You are unauthorized to access this page!");
    }
}
