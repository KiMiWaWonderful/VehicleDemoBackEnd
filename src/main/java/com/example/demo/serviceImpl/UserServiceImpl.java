package com.example.demo.serviceImpl;

import com.example.demo.pojo.User;
import com.example.demo.utils.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.UserService;
@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private IdGenerator idGenerator;

    @Resource
    private UserMapper userMapper;

    @Override
    public void addUser(User user) {
        user.setId(idGenerator.snowflakeId()+"");
        userMapper.addUser(user);
    }

    @Override
    public boolean isNameExists(String name) {
        int count = userMapper.getNameNumber(name);
        System.out.println("count:"+count);
        if(count >= 1){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public User getUserById(String id) {
        return userMapper.getUserById(id);
    }

    @Override
    public String addSalt(String password) {
        return BCrypt.hashpw(password,BCrypt.gensalt());
    }
}
