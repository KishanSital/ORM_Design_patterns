package designpatterns.structural.adapter;

import com.github.KishanSital.authenticator.models.UserModel;
import designpatterns.creational.builder.entities.uatm.User;


public interface UserModelAdapter {
    UserModel getUserModel();

    User getUser();
}
