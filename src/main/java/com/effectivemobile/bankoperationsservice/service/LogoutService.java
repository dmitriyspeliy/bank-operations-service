package com.effectivemobile.bankoperationsservice.service;

import com.effectivemobile.bankoperationsservice.model.Token;
import com.effectivemobile.bankoperationsservice.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final TokenRepository tokenRepository;

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        jwt = authHeader.substring(7);
        Optional<Token> tokenOptional = tokenRepository.findByToken(jwt);
        if (tokenOptional.isPresent()) {
            tokenOptional.get().setExpired(true);
            tokenOptional.get().setRevoked(true);
            tokenRepository.save(tokenOptional.get());
            SecurityContextHolder.clearContext();
        }
    }
}