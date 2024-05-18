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
@Schema(description = "Ответ при аутентификации")
public class AuthResponse {
    @JsonProperty("access_token")
    @Schema(description = "Токен доступа")
    String accessToken;
    @Schema(description = "Токен обновления")
    @JsonProperty("refresh_token")
    String refreshToken;
}