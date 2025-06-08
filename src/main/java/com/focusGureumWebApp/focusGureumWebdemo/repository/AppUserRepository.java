package com.focusGureumWebApp.focusGureumWebdemo.repository;

import com.focusGureumWebApp.focusGureumWebdemo.models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Integer> {
    Optional<AppUser> findByNickname(String nickname);// Use it if that is the correct field for login
}
