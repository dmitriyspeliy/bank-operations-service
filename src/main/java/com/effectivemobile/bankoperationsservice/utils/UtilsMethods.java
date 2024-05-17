package com.effectivemobile.bankoperationsservice.utils;

import com.effectivemobile.bankoperationsservice.model.User;
import com.effectivemobile.bankoperationsservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UtilsMethods {

    private final UserRepository userRepository;

    public boolean existUserByLoginEmailPhone(String login, String phone, String email) {
        return userRepository.findByLoginEmailPhone(login, email, phone).isPresent();
    }

    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

}
