package mypackage.services;

import mypackage.annotations.Service;
import mypackage.models.UserModel;

@Service("Interface for no constructor reference")
@FunctionalInterface
public interface UserModelEmpty {
    UserModel create();
}
