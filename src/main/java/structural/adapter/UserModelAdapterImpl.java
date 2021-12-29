package structural.adapter;

import entities.uatm.User;
import mypackage.models.UserModel;

import java.util.Arrays;

public class UserModelAdapterImpl implements UserModelAdapter {

    private UserModel userModel;
    private User user;

    public UserModelAdapterImpl(UserModel userModel) {
        this.userModel = userModel;
    }

    public UserModelAdapterImpl(User user) {
        this.user = user;
    }

    @Override
    public UserModel getUserModel() {
        if (user != null) {
            return convertUserToUserModel(this.user);
        }
        return this.userModel;
    }

    private UserModel convertUserToUserModel(User user) {
        return new UserModel
                .UserModelBuilder()
                .userId(user.getId())
                .username(user.getUsername())
                .password(user.getPassword().toCharArray())
                .build();
    }

    @Override
    public User getUser() {
        if (userModel != null) {
            return convertUserModelToUser(this.userModel);
        }
        return this.user;
    }

    private User convertUserModelToUser(UserModel userModel) {
        return new User
                .UserBuilder()
                .id(userModel.getUserId())
                .username(userModel.getUsername())
                .password(Arrays.toString(userModel.getPassword()))
                .build();
    }
}
