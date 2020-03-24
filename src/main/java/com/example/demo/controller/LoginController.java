package com.example.demo.controller;

import com.example.demo.pojo.UserDto;
import com.example.demo.utils.ResponseResult;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @RequestMapping(value = "/login-success")
    public ResponseResult<UserDto> loginSuccess() {
        return new ResponseResult<>(HttpStatus.OK.value(), "登录成功", null);
    }

    @GetMapping(value = "/r/r1", produces = {"text/plain;charset=UTF-8"})
//    @PreAuthorize("hasAuthority('p1')")
    public String r1(){
        return getUsername() + "访问资源1";
    }

    @GetMapping(value = "/r/r2", produces = {"text/plain;charset=UTF-8"})
//    @PreAuthorize("hasAuthority('p2')")
    public String r2(){
        return getUsername() + "访问资源2";
    }

    @GetMapping(value = "/r/rr", produces = {"text/plain;charset=UTF-8"})
    public String rr(){
        return getUsername() + "访问任意资源";
    }


    @GetMapping(value = "/r/all", produces = {"text/plain;charset=UTF-8"})
//    @PreAuthorize("hasAuthority('all')")
    public String all(){
        return getUsername() + "访问all";
    }

    //获取当前登录用户名
    private String getUsername() {
        //得到当前认证通过的用户身份
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!authentication.isAuthenticated()){
            return null;
        }
        //用户身份
        Object principal = authentication.getPrincipal();
        String username = null;
        if(principal == null){
            username = "匿名";
        }
        if(principal instanceof UserDetails){
            username = ((UserDetails) principal).getUsername();
        }else {
            username = principal.toString();
        }

        return username;
    }
}
