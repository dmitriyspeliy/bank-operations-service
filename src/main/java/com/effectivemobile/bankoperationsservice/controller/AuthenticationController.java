package com.effectivemobile.bankoperationsservice.controller;

import com.effectivemobile.bankoperationsservice.dto.AuthResponse;
import com.effectivemobile.bankoperationsservice.dto.AuthenticationRequest;
import com.effectivemobile.bankoperationsservice.dto.UserInfo;
import com.effectivemobile.bankoperationsservice.service.AuthenticationService;
import com.effectivemobile.bankoperationsservice.utils.exception.BadRequestException;
import com.effectivemobile.bankoperationsservice.utils.exception.ElemNotFound;
import com.effectivemobile.bankoperationsservice.utils.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
@Tag(name = "Аутентификация пользователя", description = "Регистрация, аутентификация, обновление токена")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Operation(summary = "Регистрация пользователя",
            description = "При успешной регистрации вернет токен доступа и рефреш токен")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Регистрация пройдена",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = AuthResponse.class)))}),
            @ApiResponse(
                    responseCode = "400",
                    description = "Если есть такое пользователь в системе. Проверка по почте, телефону и логину",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))}),
            @ApiResponse(
                    responseCode = "5XX",
                    description = "Не удалось получить ответ. Причина в сообщении",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))})})
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @RequestBody @Valid UserInfo userInfo
    ) throws BadRequestException {
        return ResponseEntity.ok(authenticationService.register(userInfo));
    }

    @Operation(summary = "Аутентификация пользователя по логину и паролю",
            description = "При успешной аутентификации вернет токен доступа и рефреш токен")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Аутентификация пройдена",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = AuthResponse.class)))}),
            @ApiResponse(
                    responseCode = "400",
                    description = "Нет юзера с таким логином",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))}),
            @ApiResponse(
                    responseCode = "403",
                    description = "Неправильный пароль или логин",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))}),
            @ApiResponse(
                    responseCode = "5XX",
                    description = "Не удалось получить ответ. Причина в сообщении",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))})})
    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) throws ElemNotFound {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @Operation(summary = "Обновление токена доступа",
            description = "Обнуляем все токены и передаем новый. Нужен Authorization хедер с токеном")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Обновление успешной пройдено",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = AuthResponse.class)))}),
            @ApiResponse(
                    responseCode = "400",
                    description = "Нет юзера с таким логином",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))}),
            @ApiResponse(
                    responseCode = "5XX",
                    description = "Не удалось получить ответ. Причина в сообщении",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))})})
    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException, ElemNotFound {
        authenticationService.refreshToken(request, response);
    }


}