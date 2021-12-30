package designpatterns.structural.adapter;

import designpatterns.creational.builder.entities.uatm.User;
import mypackage.models.UserModel;

public interface UserModelAdapter {
    UserModel getUserModel();

    User getUser();
}
