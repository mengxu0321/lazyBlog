package com.mlw.lazyblog.config;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.util.logging.Logger;

/**
 * @author oRuol
 * @Date 2020/6/23 17:07
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private UserDetailsService userDetailsService;

    @Autowired
    //认证失败处理器实现
    private AuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    //AccessDeineHandler 用来解决认证过的用户访问无权限资源时的异常
    private AccessDeniedHandler accessDeniedHandler;

    @Autowired
    //认证成功的处理器的实现
    private AuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    //AuthenticationEntryPoint 用来解决匿名用户访问无权限资源时的异常
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    //登出成功的处理器的实现
    private LogoutSuccessHandler logoutSuccessHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(authenticationEntryPoint)
                .and().authorizeRequests()
                .antMatchers("/user/*").hasAuthority("root")
                .antMatchers("/**","/").permitAll()
                .and().formLogin()
                .loginProcessingUrl("/login")//设置监听登录请求的路径
                .usernameParameter("userName")//设置用户名字段
                .passwordParameter("password")//设置密码字段
                .failureHandler(authenticationFailureHandler)
                .successHandler(authenticationSuccessHandler)
                .and().logout()
                .logoutUrl("/logout")//设置监听登出请求的路径
                .logoutSuccessHandler(logoutSuccessHandler)
                .invalidateHttpSession(true)//invalidate-session 默认为true,用户在退出后Http session失效
                .deleteCookies("JSESSIONID")
                .and().csrf().disable();//关闭csrf防护
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
        //设置userDetailService和passwordEncoder
        auth.userDetailsService(userDetailsService).passwordEncoder(encoder());
    }

    /**
     * 创建一个passwordEncoder Bean
     * @return
     */
    @Bean
    public PasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }
}
