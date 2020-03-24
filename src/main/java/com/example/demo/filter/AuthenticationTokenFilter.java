package com.example.demo.filter;

import com.example.demo.security.SpringDataUserDetailsService;
import com.example.demo.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

// 过滤器：如果认证状态通过，可以让用户携带着认证消息来访问后台API
@Component
public class AuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    SpringDataUserDetailsService userDetailsService;

    /**
     *  判断有无携带 accessToken cookie过来，
     *  如果有则解码得到对应的用户
     *  再通知服务器端这一次的请求是一个用户来发出的请求，给与响应权限
     */
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        Cookie[] cookies = httpServletRequest.getCookies();
        String token = null;
        if(httpServletRequest.getCookies() != null)
            for (int i = 0; i < cookies.length; i++) {
                if("accessToken".equals(cookies[i].getName())){
                    token = cookies[i].getValue();
                }
            }

        if(token != null && token != ""){
            Map<String, Object> map = JwtUtil.decode(token, "1139315314@qq.com", null);

            String username = (String)map.get("username");

            if(username != null && username != ""){
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (userDetails != null) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));//设置为已登录
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
