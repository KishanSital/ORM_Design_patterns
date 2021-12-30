package mypackage.views;

import mypackage.exceptions.LoginException;
import mypackage.services.LoginService;
import mypackage.services.PrintMessageService;
import mypackage.utils.IntUtilsMyPackage;
import mypackage.utils.StringUtilsMyPackage;

import java.util.Scanner;

import static mypackage.serviceImpl.TriesValidationServiceImpl.resetTriesService;
import static mypackage.serviceImpl.TriesValidationServiceImpl.triesValidation;

public final class LoginView {
    public PrintMessageService println = System.out::println; // instance method on parameter reference
    private LoginService loginService;
    private Scanner scanner;
    private String[] args;


    public LoginView(LoginService loginService) {
        this.loginService = loginService;
        this.scanner = new Scanner(System.in);
        this.args = new String[2];
    }

    public void startLoginService() {
        resetTriesService();
        do {
            providingCredentials();
            loginService.provideCredentials(args);
            authentication();
        } while (!loginService.isAuthentication());
        println.print(StringUtilsMyPackage.LOGIN_SUCCESS_MESSAGE.getStringValue());
    }


    public void providingCredentials() {

        println.print(StringUtilsMyPackage.PROVIDE_USERNAME_MESSAGE.getStringValue());
        args[IntUtilsMyPackage.USERNAME_INDEX.getIntValue()] = scanner.next();
        println.print(String.format("Hi %s", args[IntUtilsMyPackage.USERNAME_INDEX.getIntValue()]));
        println.print("");

        println.print(StringUtilsMyPackage.PROVIDE_PASSWORD_MESSAGE.getStringValue());
        char[] password = scanner.next().toCharArray();
        args[IntUtilsMyPackage.PASSWORD_INDEX.getIntValue()] = (String.valueOf(password));

    }

    public void authentication() {
        LoginException loginException = new LoginException(); // must be final or effectively final, to be used in
        //try with resource statement
        try (loginException) { // if both the close and the code within the try clause throw an exception then
            // we'd then have suppressed exceptions, with the one thrown from protected code being the primary exception
            loginService.setAuthentication(loginService.authenticationResult());
            if (!loginService.isAuthentication()) {
                throw new LoginException(StringUtilsMyPackage.INVALID_CREDENTIALS_MESSAGE.getStringValue());
            }
        } catch (LoginException e) {
            println.print(e.getMessage());
            for (Throwable t : e.getSuppressed()) {
                println.print("Suppressed: " + t.getMessage()); // looping through the suppressed exceptions
            }
        } finally {
            if (!loginService.isAuthentication()) {
                triesValidation();
            }
        }

    }
}