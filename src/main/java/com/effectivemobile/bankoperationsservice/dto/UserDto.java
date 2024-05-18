package com.effectivemobile.bankoperationsservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Dto для юзера")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UserDto {
    @Schema(description = "Номер телефона",
            example = "77017771122")
    @JsonProperty(value = "phone")
    String phone;

    @Schema(description = "Почта",
            example = "email@email.com")
    @JsonProperty(value = "email")
    String email;

    @Schema(description = "Дата рождения в формате yyyy-MM-dd",
            example = "2000-12-21")
    @JsonProperty(value = "date_of_birth")
    LocalDate dateOfBirth;

    @Schema(description = "ФИО",
            example = "Иванов Иван Иванович")
    @JsonProperty(value = "fullname")
    String fullname;

    @Schema(description = "Логин",
            example = "login")
    @JsonProperty(value = "login")
    String login;

    @Schema(description = "Пароль",
            example = "1239010")
    @JsonProperty(value = "password")
    String password;

    @Schema(description = "Текущий баланс",
            example = "3")
    @JsonProperty(value = "balance")
    BigDecimal balance;
}
