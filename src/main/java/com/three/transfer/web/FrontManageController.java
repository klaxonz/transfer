package com.three.transfer.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

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

    @RequestMapping(value = "share/{linkUrl}")
    public String share(@PathVariable String linkUrl) {
        return "frontend/share";
    }





}
