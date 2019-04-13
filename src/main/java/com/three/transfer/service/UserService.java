package com.three.transfer.service;


import com.three.transfer.entity.User;

public interface UserService {

    /**
     * 添加用户
     * @param user
     * @return 0:用户存在 1:添加成功
     */
    int addUser(User user);

    /**
     * 通过id获取用户
     * @param userId
     * @return
     */
    User getUserById(int userId);

    /**
     * 通过用户名查询用户是否存在
     * @param username
     * @return
     */
    public User getUserByName(String username);

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
    int removeUserById(Long userId);

    /**
     * 判断是否存在某个用户名
     * @param username
     * @return
     */
    public boolean isExist(String username);

    /**
     * 验证是否信息是否匹配
     * @param user
     * @return 0:密码正确 -1:密码错误
     */
    public int userCheckPassword(User user, User checkUser);

}
