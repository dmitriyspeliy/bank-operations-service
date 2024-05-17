package com.effectivemobile.bankoperationsservice.service;

import com.effectivemobile.bankoperationsservice.model.User;
import com.effectivemobile.bankoperationsservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Optional;

import static com.effectivemobile.bankoperationsservice.utils.LogFormatter.logInfo;

@Component
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        if (login == null || login.equals("")) return null;
        Optional<User> authUser = userRepository.findByLogin(login);
        if (authUser.isPresent()) {
            logInfo("Starting session for " + login.toUpperCase());
            return authUser.map(user -> new org.springframework.security.core.userdetails.User(
                    user.getLogin(), user.getPassword(), new ArrayList<>())).get();
        } else {
            return null;
        }

    }
}
