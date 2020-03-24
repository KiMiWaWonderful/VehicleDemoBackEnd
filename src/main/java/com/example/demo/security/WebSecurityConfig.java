package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    //定义用户信息服务（查询用户信息）
//    @Bean
//    public UserDetailsService userDetailsService(){
//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//        manager.createUser(User.withUsername("zhangsan").password("123").authorities("p1").build());
//        manager.createUser(User.withUsername("lisi").password("456").authorities("p2").build());
//        return manager;
//    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    //定义解码器
    @Bean
    public PasswordEncoder passwordEncoder() {
       // return NoOpPasswordEncoder.getInstance();
        return new BCryptPasswordEncoder();
    }

    //定义登陆成功返回信息
    private class AxiousAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

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
                //解决跨域
                .csrf().disable()
                //要以这个开始
                .authorizeRequests()
//                .antMatchers("/r/r1").hasAuthority("p1")
//                .antMatchers("/r/r2").hasAuthority("p2")
                //只允许登录
                .antMatchers("/users/login_page", "/register","/ws/*","/websocket/*").permitAll()
                //所有/r/**的请求必须认证通过
//                .antMatchers("/r/**").authenticated()
                //除了/r/**，其他的请求可以访问
//                .anyRequest().permitAll()
                //其余方法都要认证
                .anyRequest().authenticated()
                .and()
                //允许表单登录
                .formLogin()
                //登录页面(不会再跳转到默认地址)
                .loginPage("/users/login_page")
                .successHandler(new AxiousAuthSuccessHandler())
                .failureHandler(new AxiousAuthFailHandler())
                //制定登录处理的url，也就是用户名、密码表单提交的目标路径
                .loginProcessingUrl("/login").usernameParameter("username").passwordParameter("password")
                //自定义登录成功的页面地址
                .successForwardUrl("/login-success")
                //允许所有用户访问我们的登录页
                .permitAll()


        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .logout()
                //自定义退出url
                .logoutUrl("/logout");
    }
}
