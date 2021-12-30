package mypackage.application;

import mypackage.models.UserModel;
import mypackage.serviceImpl.LoginServiceImpl;
import mypackage.serviceImpl.UserSessionServiceImpl;
import mypackage.services.LoginService;
import mypackage.services.UserModelEmpty;
import mypackage.views.LoginView;

public final class MyPackageApplication {
    public static void startLoginService(UserModel expectedUser) {

        if (expectedUser == null) {
            System.out.println("\n Something went wrong please try again later.");
            System.exit(0);
        }

        //Constructor reference
        UserModelEmpty userModelEmpty = UserModel::new;

        new UserSessionServiceImpl(expectedUser);
        LoginService loginServiceImpl = new LoginServiceImpl(userModelEmpty.create());
        new LoginView(loginServiceImpl).startLoginService();
    }
}
