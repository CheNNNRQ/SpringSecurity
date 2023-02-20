package com.diodi.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author diodi
 * @create 2023-02-10-11:12
 */
@Data
@NoArgsConstructor
public class UserLogin implements UserDetails {

    private User user;

    //存储权限信息
    private List<String> permissions;

    @JSONField(serialize = false)
    private List<GrantedAuthority> Authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (Authorities != null){
            return Authorities;
        }
        List<SimpleGrantedAuthority> authorities = permissions
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return authorities;
    }

    @Override
    public String getUsername() {
        return user.getUserName();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
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
}
