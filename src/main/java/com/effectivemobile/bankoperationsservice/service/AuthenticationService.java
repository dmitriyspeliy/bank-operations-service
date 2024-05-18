package com.effectivemobile.bankoperationsservice.service;

import com.effectivemobile.bankoperationsservice.dto.AuthResponse;
import com.effectivemobile.bankoperationsservice.dto.AuthenticationRequest;
import com.effectivemobile.bankoperationsservice.dto.UserInfo;
import com.effectivemobile.bankoperationsservice.mapper.UserMapper;
import com.effectivemobile.bankoperationsservice.model.Token;
import com.effectivemobile.bankoperationsservice.model.User;
import com.effectivemobile.bankoperationsservice.repository.TokenRepository;
import com.effectivemobile.bankoperationsservice.repository.UserRepository;
import com.effectivemobile.bankoperationsservice.utils.enums.TokenType;
import com.effectivemobile.bankoperationsservice.utils.exception.BadRequestException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(UserInfo userInfo) throws BadRequestException {
        User user = userService.createUser(userInfo);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(user, jwtToken);
        return AuthResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getLogin(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByLogin(request.getLogin());
        String jwtToken = null;
        String refreshToken = null;
        if (user.isPresent()) {
            jwtToken = jwtService.generateToken(user.get());
            refreshToken = jwtService.generateRefreshToken(user.get());
            revokeAllUserTokens(user.get());
            saveUserToken(user.get(), jwtToken);
        }
        return AuthResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String refreshToken;
        String login;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) return;
        refreshToken = authHeader.substring(7);
        login = jwtService.extractLogin(refreshToken);
        var user = userRepository.findByLogin(login);
        if (user.isPresent() && jwtService.isTokenValid(refreshToken, user.get())) {
            var accessToken = jwtService.generateToken(user.get());
            revokeAllUserTokens(user.get());
            saveUserToken(user.get(), accessToken);
            var authResponse = AuthResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
            new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
        }
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty()) return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
}