package com.example.demo.security;

import com.example.demo.mapper.UserDao;
//import com.example.demo.mapper.UserMapper;
import com.example.demo.pojo.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpringDataUserDetailsService implements UserDetailsService {

    @Autowired
    UserDao userDao;

//    @Resource
//    private UserMapper userMapper;

    //根据账号查询用户信息
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

//        Example example = new Example(UserDto.class);
//        example.createCriteria().andEqualTo("username",username);
        UserDto userDto = userDao.getUserByUsername(username);
        if(userDto == null){
            //返回null,由provider来抛出异常
            return null;
        }

        //根据用户id查询用户的权限
        List<String> permissions = userDao.findPermissionByUserId(userDto.getId());
        //将permissions转换成数组
        String[] permissionArray = new String[permissions.size()];
        permissions.toArray(permissionArray);
        for (int i = 0; i < permissionArray.length; i++) {
            System.out.print(permissionArray[i] + " ");
        }
        //System.out.println(permissionArray);

       // System.out.println("username = " + username);
        //暂时采用模拟方式
        //密码要填加密后的
      //  UserDetails userDetails = User.withUsername("zhangsan").password("$2a$10$EYZDEq/HKEs/A7.65ui7HuNCSR1G/I9RR9WoikhHfC5TxoYTAVIcC").authorities("p1").build();
        //密码要传入BCypt格式
        UserDetails userDetails = User.withUsername(userDto.getUsername()).password(userDto.getPassword()).authorities(permissionArray).build();
        return userDetails;
    }
}
