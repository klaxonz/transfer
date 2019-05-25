package com.three.transfer.dao;

import com.three.transfer.entity.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer userId);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer userId);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    /**
     * 通过用户名查询用户
     * @param username
     * @return
     */
    User getUserByName(String username);

    /**
     * 通过用户名和密码查找用户
     * @param username
     * @param password
     * @return
     */
    User getUserByNameAndPwd(@Param("username") String username, @Param("password") String password);

}