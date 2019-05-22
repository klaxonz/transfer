package com.three.transfer.dao;


import com.three.transfer.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao {

    /**
     * 插入一个新的用户
     * @param user
     * @return
     */
    int insertUser(User user);

    /**
     * 通过id获取User
     * @param id
     * @return
     */
    User getUserById(int id);

    /**
     * 通过id删除用户
     * @param id
     * @return
     */
    int deleteUserById(Long id);

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    int updateUser(User user);

    /**
     * 通过用户名查询用户
     * @param username
     * @return
     */
    User getUserByName(String username);


}
