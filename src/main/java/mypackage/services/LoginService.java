package mypackage.services;

import mypackage.annotations.Service;
import mypackage.models.UserModel;

import java.util.function.Predicate;

@Service
public interface LoginService {
    void init();
    void provideCredentials(String [] args);
    boolean authenticationResult();
    boolean authenticateUser(Predicate<UserModel> loggingIn);
    String getLoggedInUser();
    boolean isAuthentication();
    void setAuthentication(boolean authentication);
}
