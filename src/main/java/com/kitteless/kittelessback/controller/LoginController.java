package com.kitteless.kittelessback.controller;

import com.kitteless.kittelessback.model.LoginResponse;
import com.kitteless.kittelessback.model.User;
import com.kitteless.kittelessback.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * ログイン画面
 */
@RestController
public class LoginController {
    @Autowired
    UserService userService;

    @PostMapping(value = "/login")
    public LoginResponse login(@RequestBody User user) {
        return userService.login(user);
    }
}
