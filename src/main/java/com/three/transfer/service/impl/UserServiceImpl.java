package com.three.transfer.service.impl;

import com.three.transfer.dao.UserDao;
import com.three.transfer.entity.User;
import com.three.transfer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;


    @Override
    public int addUser(User user) {
        if (isExist(user.getUserName())) {
            return 0;
        }

        return userDao.insertUser(user);
    }

    @Override
    public User getUserById(int userId) {
        return userDao.getUserById(userId);
    }

    @Override
    public User getUserByName(String username) {
        return userDao.getUserByName(username);
    }

    @Override
    public int modifyUser(User user) {
        return userDao.updateUser(user);
    }

    @Override
    public int removeUserById(Long userId) {
        return userDao.deleteUserById(userId);
    }

    @Override
    public boolean isExist(String username) {
        return userDao.getUserByName(username) != null;
    }

    @Override
    public int userCheckPassword(User user, User checkUser) {
        String userPassword = user.getUserPassword();
        String checkUserPassword = checkUser.getUserPassword();
        if (!userPassword.equals(checkUserPassword)) {
            return -1;
        }

        return 0;
    }
}
