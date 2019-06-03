package com.phantom.seckill.service;

import com.phantom.seckill.common.RedisAdapter;
import com.phantom.seckill.entity.User;
import com.phantom.seckill.exception.GlobalException;
import com.phantom.seckill.mapper.UserMapper;
import com.phantom.seckill.utils.MD5Util;
import com.phantom.seckill.utils.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.List;


@Service
public class UserService {

    @Autowired
    UserMapper userMapper;
    @Autowired
    RedisAdapter redisAdapter;

    public User getUserById(Long id) {
        return userMapper.selectByPrimaryKey(id);
    }

    public List<User> getAllUsers(){return userMapper.getAllUsers();}

    public  boolean registerUser(Long id, String nickname, String password) {
        if (userMapper.selectByPrimaryKey(id) != null) {
            return false;
        }
        User user = new User();
        user.setId(id);
        user.setNickname(nickname);
        String salt = UUIDUtil.getSalt();
        user.setSalt(salt);
        user.setPassword(MD5Util.encrypt(password + salt)); // 加盐
        user.setRegisterDate(new Date());
        return userMapper.insertSelective(user) > 0;
    }



    public User login(Long id, String password) {
        User user;
        if ((user = userMapper.selectByPrimaryKey(id)) == null) {
            throw new GlobalException("登录失败，请检查手机是否输入错误");
        }

        String salt = user.getSalt();

        if (!MD5Util.encrypt(password + salt).equals(user.getPassword())) {
            throw new GlobalException("登录失败，请检查密码是否输入错误");
        }
        return user;
    }
}
