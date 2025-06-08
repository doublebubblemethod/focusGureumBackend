package com.focusGureumWebApp.focusGureumWebdemo.services;

import com.focusGureumWebApp.focusGureumWebdemo.models.AppUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AppUserDetails implements UserDetails {

    private final String nickname;
    private final String password;
    private final List<GrantedAuthority> authorities;

    public AppUserDetails(AppUser appUser) {
        this.nickname = appUser.getNickname();
        this.password = appUser.getPassword();
        this.authorities = Stream.of(appUser.getRole().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return nickname;
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