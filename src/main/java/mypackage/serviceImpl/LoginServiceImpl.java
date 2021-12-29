package mypackage.serviceImpl;


import mypackage.models.UserModel;
import mypackage.services.LoginService;
import mypackage.utils.IntUtilsMyPackage;

import java.util.Arrays;
import java.util.function.Predicate;

import static mypackage.serviceImpl.UserSessionServiceImpl.LOGIN_PASSWORD;
import static mypackage.serviceImpl.UserSessionServiceImpl.LOGIN_USERNAME;

public final class LoginServiceImpl implements LoginService {
    public boolean isAuthentication;
    private boolean isLoggedIn;
    private UserModel userModel;

    public LoginServiceImpl(UserModel userModel) {
        super();
        this.userModel = userModel;
        init();
    }

    public void init() {
    }

    @Override
    public void provideCredentials(String... args) {
        userModel.setUsername(args[IntUtilsMyPackage.USERNAME_INDEX.getIntValue()]);
        char[] chars = args[IntUtilsMyPackage.PASSWORD_INDEX.getIntValue()].toCharArray();
        userModel.setPassword(chars);
    }

    @Override
    public boolean authenticationResult() {
        isAuthentication = authenticateUser(k -> {
            if ((LOGIN_USERNAME.equalsIgnoreCase(k.getUsername())) &&
                    (Arrays.equals(LOGIN_PASSWORD, k.getPassword()))) {
                return true;
            } else {
                return false;
            }
        });
        return isAuthentication;
    }

    @Override
    public boolean authenticateUser(Predicate<UserModel> loggingIn) {
        isLoggedIn = false;
        if (loggingIn.test(userModel)) {
            isLoggedIn = true;
        }
        return isLoggedIn;
    }

    @Override
    public String getLoggedInUser() {
        return LOGIN_USERNAME;
    }

    @Override
    public boolean isAuthentication() {
        return isAuthentication;
    }

    @Override
    public void setAuthentication(boolean authentication) {

    }
}
