package com.mlw.lazyblog.entity;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author oRuol
 * @Date 2020/6/23 15:58
 */
@Slf4j
public class User implements UserDetails {
    private int userId;
    private String password;
    private String userName;
    private String userAuthority;

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> list = new ArrayList<>();
        Arrays.asList(userAuthority.split(",")).
                forEach((String authority)->{
                    list.add(new SimpleGrantedAuthority(authority));
                });
//        log.info(this.password);
        return list;
    }

    public User() {
        super();
    }

    @Override
    public String getUsername() {
        return this.userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", password='" + password + '\'' +
                ", userName='" + userName + '\'' +
                ", userAuthority='" + userAuthority + '\'' +
                '}';
    }
}
