package com.example.peoplemeals.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class LoginController {

    @GetMapping("login")
    public String getLogin() {
        throw new SecurityException("Unauthorized: please login before proceed");
    }

    @GetMapping("success")
    public String successLogin() {
        throw new SecurityException("Login successful!", new Throwable("success"));
    }

    @GetMapping("logout_ok")
    public String logout() {
        throw new SecurityException("You are now logged out.", new Throwable("success"));
    }

    @PostMapping("failure")
    public String failLogin() {
        return "redirect:login";
    }
}
