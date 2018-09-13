package com.xiang.activiti.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/login")
public class LoginController
{
    @RequestMapping(params = "toLogin")
    public String login() {
        return "index";
    }
}
