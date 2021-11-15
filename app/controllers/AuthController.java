package controllers;

import forms.AuthFormData;
import models.Lecturer;
import play.data.DynamicForm;
import play.data.Form;
import play.data.FormFactory;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.LecturerService;

import javax.inject.Inject;

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
                return redirect(routes.HomeController.index());
            }
        }

        Form<AuthFormData> loginFormWithError = formFactory.form(AuthFormData.class).withGlobalError("ERROR");
        return ok(views.html.login.render(loginFormWithError));
    }

    public Result logout(){
        return redirect(routes.AuthController.showLoginForm());
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

        Form<AuthFormData> loginFormWithError = formFactory.form(AuthFormData.class).withGlobalError("ERROR");
        return ok(views.html.resetPassword.render(loginFormWithError));
    }
}
