package com.three.transfer.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/transfer", method = RequestMethod.GET)
public class FrontManageController {

    @RequestMapping("")
    public String index() {
        return "frontend/index";
    }


    @RequestMapping("/register")
    public String register() {
        return "frontend/register";
    }

    @RequestMapping("/login")
    public String login() {
        return "frontend/login";
    }

    @RequestMapping("/main")
    public String main() {
        return "frontend/main";
    }

    @RequestMapping("/share")
    public String share() {
        return "frontend/share";
    }





}
