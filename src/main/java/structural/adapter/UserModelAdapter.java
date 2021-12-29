package structural.adapter;

import entities.uatm.User;
import mypackage.models.UserModel;

public interface UserModelAdapter {
    UserModel getUserModel();

    User getUser();

}
