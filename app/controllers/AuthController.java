package controllers;

import forms.AuthFormData;
import models.Lecturer;
import play.data.DynamicForm;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.LecturerService;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

public class AuthController extends Controller {
    private FormFactory formFactory;
    private LecturerService lecturerService;

    @Inject
    public AuthController(FormFactory formFactory, LecturerService lecturerService) {
        this.formFactory = formFactory;
        this.lecturerService = lecturerService;
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
                return redirect(routes.CourseInformationController.showCourseInformationForm(lecturer.id)).addingToSession(request, sessionValuesMap);
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
}
