package com.example.demo.security;

import com.example.demo.filter.AuthenticationTokenFilter;
import com.example.demo.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    SpringDataUserDetailsService springDataUserDetailsService;

    @Autowired
    AuthenticationTokenFilter authenticationTokenFilter;


    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    //定义解码器
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //定义登陆成功返回信息，并且让用户之后请求后端API携带cookie获取认证信息
    private class AxiousAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

            // 登录成功则给浏览器设置cookie，下次访问服务器的时候携带该cookie以获取认证的信息
            String username = request.getParameter("username");
            Map<String,Object> attrMap = new HashMap<>();
            attrMap.put("username",username);
            String encode = JwtUtil.encode("1139315314@qq.com", attrMap, null);
            Cookie cookie = new Cookie("accessToken", encode);
            cookie.setPath("/");
            response.addCookie(cookie);

            //User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            System.out.println("商户[" + SecurityContextHolder.getContext().getAuthentication().getPrincipal() +"]登陆成功！");
            //登陆成功后移除session中验证码信息
            request.getSession().removeAttribute("codeValue");
            request.getSession().removeAttribute("codeTime");

            response.setContentType("application/json;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.write("{\"status\":\"ok\",\"msg\":\"登录成功\"}");
            out.flush();
            out.close();
        }
    }

    //定义登陆失败返回信息
    private class AxiousAuthFailHandler extends SimpleUrlAuthenticationFailureHandler {
        @Override
        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
            //登陆失败后移除session中验证码信息
            request.getSession().removeAttribute("codeValue");
            request.getSession().removeAttribute("codeTime");

            response.setContentType("application/json;charset=utf-8");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            PrintWriter out = response.getWriter();
            out.write("{\"status\":\"error\",\"msg\":\"请检查用户名、密码或验证码是否正确\"}");
            out.flush();
            out.close();
        }
    }

    //定义异常返回信息
    public class UnauthorizedEntryPoint implements AuthenticationEntryPoint {
        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
            response.sendError(HttpStatus.UNAUTHORIZED.value(),authException.getMessage());
        }

    }

    //安全拦截机制（最重要）
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // 解决跨域
                .cors()
                .and()
                .csrf().disable()

                // 对请求进行授权
                .authorizeRequests()
                // 允许放行以下请求
                .antMatchers("/users/login_page", "/register","/ws/*","/websocket/*").permitAll()
                // 其他全部请求需要认证
                .anyRequest().authenticated()
                .and()

                // 允许表单登录
                .formLogin()
                // 制定登录处理的url，也就是用户名、密码表单提交的目标路径
                .loginProcessingUrl("/login").usernameParameter("username").passwordParameter("password")
                .successHandler(new AxiousAuthSuccessHandler())  // 成功如何处理
                .failureHandler(new AxiousAuthFailHandler())     // 失败如何处理
                // 允许所有用户访问我们的登录页
                .permitAll()
                .and()

                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()

                .logout()
                //自定义退出url
                .logoutUrl("/logout");

        // 添加过滤器
        http.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        // 禁用默认规则
        //super.configure(auth);
        // 验证密码
        builder.userDetailsService(springDataUserDetailsService).passwordEncoder(passwordEncoder());
    }
}
