package com.example.peoplemeals.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class LoginController {

    public static final String loginMessage = "Please login before proceeding.";
    public static final String successMessage = "Login successful!";
    public static final String logoutMessage = "You are now logged out.";

    @GetMapping("login")
    public String getLogin() {
        throw new SecurityException(loginMessage);
    }

    @GetMapping("success")
    public String successLogin() {
        throw new SecurityException(successMessage, new Throwable("success"));
    }

    @GetMapping("logout_ok")
    public String logout() {
        throw new SecurityException(logoutMessage, new Throwable("success"));
    }

    @PostMapping("failure")
    public String failLogin() {
        return "redirect:login";
    }
}
