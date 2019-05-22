package com.three.transfer.dao;

import com.three.transfer.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserDaoTest {
    @Autowired
    private UserDao userDao;

    @Test
    public void testInsertUser() {
        User user = new User();
        user.setUserName("test");
        user.setUserPassword("1111");
        user.setUserEmail("1160359869@qq.com");
        user.setCreateTime(new Date());
        user.setLastEditTime(new Date());
        int res = userDao.insertUser(user);
        System.out.println(res);
    }

    @Test
    public void testQueryUserById() {
        User user = userDao.getUserById(13);
        System.out.println(user.getUserName());
    }

    @Test
    public void testUpdateUser() {
        User user = userDao.getUserById(1);
        user.setCreateTime(new Date());
        user.setUserName("new name");
        user.setUserPassword("new password");
        user.setLastEditTime(new Date());
        user.setUserEmail("1160359869@qq.com");
        user.setUsedCapacity(200L);
        int res = userDao.updateUser(user);
        System.out.println(res);
    }

    @Test
    public void testDeleteUserById() {
        Long userId = 2L;
        int res = userDao.deleteUserById(userId);
        System.out.println(res);
    }

    @Test
    public void testGetUserByName() {
        String username = "Jan5";
        User user = userDao.getUserByName(username);
        System.out.println(user);
    }


}
