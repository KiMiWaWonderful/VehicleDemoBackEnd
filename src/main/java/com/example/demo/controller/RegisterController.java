package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.pojo.User;
import com.example.demo.service.UserService;
import com.example.demo.utils.IdGenerator;
import com.example.demo.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterController {

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private UserService userService;

    //注册
    @PostMapping("/register")
    // @ResponseBody
    @CrossOrigin
    public ResponseResult<User> addStudent(@RequestBody String userJson){
        System.out.println(userJson);
        User user = JSONObject.parseObject(userJson,User.class);
        System.out.println(user);

        //判断用户是否存在
        if(userService.isNameExists(user.getUsername())){
            System.out.println("用户已经存在");
            return new ResponseResult<>(HttpStatus.BAD_REQUEST.value(),"用户已经存在",null);
        }else {
            System.out.println("注册成功");
            String saltPassword = userService.addSalt(user.getPassword());
            user.setPassword(saltPassword);
            userService.addUser(user);
            User user1 = userService.getUserById(user.getId());
            return new ResponseResult<>(HttpStatus.OK.value(),"注册成功",user1);
        }
    }
}
