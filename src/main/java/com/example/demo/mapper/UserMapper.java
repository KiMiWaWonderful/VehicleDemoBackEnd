package com.example.demo.mapper;

import com.example.demo.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

public interface UserMapper extends Mapper<User> {

    @Insert("insert into t_user (id,username,password) VALUES (#{id,jdbcType=VARCHAR},#{username,jdbcType=VARCHAR},#{password,jdbcType=VARCHAR})")
    void addUser(User user);

    @Select("select * from t_user where id = #{id}")
    User getUserById(@Param("id")String id);

    @Select("select count(username) as count from t_user where username = #{username}")
    int getNameNumber(@Param("username")String username);
}