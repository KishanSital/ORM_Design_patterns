package serviceImpl;

import designpatterns.creational.builder.entities.uatm.User;
import services.UatmSessionService;

public class UatmSessionServiceImpl implements UatmSessionService {

    public static User user;

    public UatmSessionServiceImpl(User user) {
        this.user = user;
    }
}
