package com.kitteless.kittelessback.service;

import com.kitteless.kittelessback.model.LoginResponse;
import com.kitteless.kittelessback.model.RegisterResponse;
import com.kitteless.kittelessback.model.User;
import com.kitteless.kittelessback.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public RegisterResponse register(User user) {
        userRepository.save(user);

        RegisterResponse registerResponse = new RegisterResponse();
        registerResponse.setResult("success");
        return registerResponse;
    }

    public LoginResponse login(User user) {
        User loginUser = userRepository.findByPassword(user.getPassword());

        //失敗時の処理

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setResult("success");
        loginResponse.setResult(loginUser.getId());
        return loginResponse;
    }
}
