package com.three.transfer.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.three.transfer.entity.User;
import com.three.transfer.service.UserService;
import com.three.transfer.util.HttpServletRequestUtil;;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/transfer")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/registercheck", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> registerCheck(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        String userStr = HttpServletRequestUtil.getString(request, "userStr");
        ObjectMapper mapper = new ObjectMapper();
        User user = null;
        try {
            user = mapper.readValue(userStr, User.class);
            user.setCreateTime(new Date());
            user.setLastEditTime(new Date());
            //判断用户名是否存在

            int res = userService.addUser(user);
            if (res <= 0) {
                modelMap.put("success", false);
                modelMap.put("errMsg", "注册失败");
                modelMap.put("errCode", 0);
            }else {
                modelMap.put("success", true);
            }

        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
        }
        return modelMap;
    }


    @RequestMapping(value = "/logincheck", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> userLogin(HttpServletRequest request) {
        Map<String,Object> modelMap = new HashMap<>();
        String userStr = HttpServletRequestUtil.getString(request, "userStr");
        ObjectMapper mapper = new ObjectMapper();
        User checkUser = null;
        try {
            checkUser = mapper.readValue(userStr, User.class);
            User user = userService.getUserByName(checkUser.getUserName());
            if (user != null) {
                int res = userService.userCheckPassword(user, checkUser);
                if (res == -1) {
                    //密码错误
                    modelMap.put("success", false);
                    modelMap.put("errCode", res);
                } else {
                    modelMap.put("success", true);
                    //登录成功后将给用户添加进session中
                    HttpSession session = request.getSession();
                    session.setAttribute("user", user);
                }
            } else {
                //用户不存在
                modelMap.put("success", false);
                modelMap.put("errCode", -2);
            }

        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
        }
        return modelMap;
    }


    @RequestMapping(value = "/getuserinfo", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getUserInfo(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        user = userService.getUserById(user.getUserId());

        if (user != null) {
            modelMap.put("success", true);
            modelMap.put("totalCapacity", user.getTotalCapacity());
            modelMap.put("usedCapacity", user.getUsedCapacity());
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "获取用户信息失败");
        }
        return modelMap;
    }

}
