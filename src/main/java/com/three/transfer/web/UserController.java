package com.three.transfer.web;

import com.three.transfer.common.Const;
import com.three.transfer.common.ResponseCode;
import com.three.transfer.common.ServerResponse;
import com.three.transfer.entity.User;
import com.three.transfer.service.UserService;
import com.three.transfer.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    @RequestMapping(value = "/register")
    @ResponseBody
    public ServerResponse register(User user) {
        return userService.register(user);
    }


    @RequestMapping(value = "/login")
    @ResponseBody
    public ServerResponse<String> userLogin(HttpSession session, @RequestParam("username") String username,
                                    @RequestParam("password") String password) {

        ServerResponse<String> result = userService.login(session, username, password);
        return result;
    }


    @RequestMapping(value = "/getuserinfo")
    @ResponseBody
    public ServerResponse<UserVo> getUserInfo(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录");
        }
        return userService.getUserInfo(user);
    }

    @GetMapping("/logout")
    public ServerResponse logout(HttpSession session) {
        return userService.logout(session);
    }

}
