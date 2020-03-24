package com.example.demo.service;

import com.example.demo.pojo.User;

public interface UserService{

    //添加学生
    public void addUser(User user);

    //判断是否存在相同的用户名
    boolean isNameExists(String name);

    //根据id得到该学生的全部信息
    User getUserById(String id);

    //为密码加盐
    String addSalt(String password);
}
