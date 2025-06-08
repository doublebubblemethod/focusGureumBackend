package com.focusGureumWebApp.focusGureumWebdemo.services;

import com.focusGureumWebApp.focusGureumWebdemo.models.AppUser;
import com.focusGureumWebApp.focusGureumWebdemo.repository.AppUserRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Service
public class AppUserService implements UserDetailsService {

    private final AppUserRepository repository;
    private final PasswordEncoder passwordEncoder;
    public AppUserService(@Lazy PasswordEncoder passwordEncoder, AppUserRepository repository) {
        this.passwordEncoder = passwordEncoder;
        this.repository = repository;
    }
    @Override
    public UserDetails loadUserByUsername(String nickname) throws UsernameNotFoundException {
        Optional<AppUser> userDetail = repository.findByNickname(nickname);
        return userDetail.map(AppUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + nickname));
    }

    public String addUser(AppUser appUser) {
        // Encode password before saving the user
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        repository.save(appUser);
        return "User Added Successfully";
    }
}