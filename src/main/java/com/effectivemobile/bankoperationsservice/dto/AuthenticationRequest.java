package com.effectivemobile.bankoperationsservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Запрос на аутентификацию")
public class AuthenticationRequest {
    @JsonProperty("login")
    @Schema(description = "Логин пользователя")
    String login;
    @JsonProperty("password")
    @Schema(description = "Пароль пользователя")
    String password;
}