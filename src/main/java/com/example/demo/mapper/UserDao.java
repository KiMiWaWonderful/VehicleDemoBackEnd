package com.example.demo.mapper;

import com.example.demo.pojo.PermissionDto;
import com.example.demo.pojo.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public UserDto getUserByUsername(String username){
        String sql = "select id,username,password from t_user where username = ?";
        List<UserDto> list = jdbcTemplate.query(sql, new Object[]{username}, new BeanPropertyRowMapper<>(UserDto.class));
        if(list == null && list.size() <= 0){
            return null;
        }
        return list.get(0);
    }

    //根据用户id查询用户权限
    public List<String> findPermissionByUserId(String userId) {
        String sql = "select * from t_permission where id in(\n" +
                "    select permission_id from t_role_permission where role_id in (\n" +
                "        select role_id from t_user_role where user_id = ?\n" +
                "        )\n" +
                "    )";
        List<PermissionDto> list = jdbcTemplate.query(sql, new Object[]{userId}, new BeanPropertyRowMapper<>(PermissionDto.class));
        List<String> permissions = new ArrayList<>();
        list.iterator().forEachRemaining(c -> permissions.add(c.getCode()));
        return permissions;
    }

}
