package mypackage.services;

import mypackage.annotations.Service;
import mypackage.models.UserModel;

@Service("Interface for constructor reference with arguments")
@FunctionalInterface
public interface UserModelWithArguments {
    UserModel create(Long userId, String username, char[] password);
}
