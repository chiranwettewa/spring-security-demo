package com.chiran.springsecurityclient.service;

import com.chiran.springsecurityclient.entity.User;
import com.chiran.springsecurityclient.model.UserModel;

public interface UserService {
    User registerUser(UserModel userModel);

    void saveVerificationTokenForUser(User user, String token);

}
