package com.prgrms.zzalmyu.domain.user.presentation.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginWebController {

    @GetMapping("/api/v1/login")
    public String login() {
        return "login";
    }
}
