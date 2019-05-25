package com.three.transfer.service.impl;

import com.three.transfer.common.Const;
import com.three.transfer.common.ServerResponse;
import com.three.transfer.dao.UserMapper;
import com.three.transfer.entity.User;
import com.three.transfer.service.UserService;
import com.three.transfer.util.DateTimeUtil;
import com.three.transfer.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;


    @Override
    public ServerResponse register(User user) {
        if (isExist(user.getUserName())) {
            return ServerResponse.createByErrorMessage("用户已存在");
        }
        user.setTotalCapacity(Const.CAPACITY.NORMAL_CAPACITY);
        user.setUsedCapacity(0L);
        int rowCount = userMapper.insert(user);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("创建用户成功");
        }
        return ServerResponse.createByErrorMessage("创建用户失败");
    }

    @Override
    public ServerResponse<String> login(HttpSession session, String username, String password) {
        User user = userMapper.getUserByNameAndPwd(username, password);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户名或密码错误");
        }
        session.setAttribute(Const.CURRENT_USER, user);
        return ServerResponse.createBySuccessMessage("登录成功");
    }

    public ServerResponse<UserVo> getUserInfo(User user) {
        user = userMapper.selectByPrimaryKey(user.getUserId());
        if (user == null) {
            return ServerResponse.createByErrorMessage("获取用户信息失败");
        }
        UserVo userVo = assembleUserVo(user);
        return ServerResponse.createBySuccess("获取用户信息成功", userVo);
    }

    private UserVo assembleUserVo(User user) {
        UserVo userVo = new UserVo();
        userVo.setId(user.getUserId());
        userVo.setUsername(user.getUserName());
        userVo.setEmail(user.getUserEmail());
        userVo.setCreateTime(DateTimeUtil.dateToStr(user.getCreateTime()));
        userVo.setLastEditTime(DateTimeUtil.dateToStr(user.getLastEditTime()));
        userVo.setTotalCapacity(user.getTotalCapacity());
        userVo.setUsedCapacity(user.getUsedCapacity());
        return userVo;
    }


    public ServerResponse logout(HttpSession session) {
        session.removeAttribute(Const.CURRENT_USER);
        session.invalidate();
        return ServerResponse.createBySuccessMessage("退出登录成功");
    }

    @Override
    public User getUserByName(String username) {
        return userMapper.getUserByName(username);
    }

    @Override
    public int modifyUser(User user) {
        return userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public int removeUserById(Integer userId) {
        return userMapper.deleteByPrimaryKey(userId);
    }

    @Override
    public boolean isExist(String username) {
        return userMapper.getUserByName(username) != null;
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
