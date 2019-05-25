package com.three.transfer.service;


import com.three.transfer.common.ServerResponse;
import com.three.transfer.entity.User;
import com.three.transfer.vo.UserVo;

import javax.servlet.http.HttpSession;

public interface UserService {


    /**
     * 添加用户
     * @param user
     * @return
     */
    ServerResponse register(User user);

    /**
     * 用户登录
     * @param session
     * @param username
     * @param password
     * @return
     */
    ServerResponse<String> login(HttpSession session, String username, String password);


    /**
     * 获取用户信息
     * @param user
     * @return
     */
    ServerResponse<UserVo> getUserInfo(User user);


    /**
     * 退出登录
     * @param session
     * @return
     */
    ServerResponse logout(HttpSession session);

    /**
     * 通过用户名查询用户是否存在
     * @param username
     * @return
     */
    User getUserByName(String username);

    /**
     * 修改用户信息
     * @param user
     * @return
     */
    int modifyUser(User user);

    /**
     * 通过id删除用户
     * @param userId
     * @return
     */
    int removeUserById(Integer userId);

    /**
     * 判断是否存在某个用户名
     * @param username
     * @return
     */
     boolean isExist(String username);

    /**
     * 验证是否信息是否匹配
     * @param user
     * @return 0:密码正确 -1:密码错误
     */
     int userCheckPassword(User user, User checkUser);

}
