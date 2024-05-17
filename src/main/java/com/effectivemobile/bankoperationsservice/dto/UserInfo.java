package com.effectivemobile.bankoperationsservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Dto для информации про юзера")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserInfo {

    @Schema(description = "Номер телефона",
            example = "77017771122")
    @JsonProperty(value = "phone")
    @Size(max = 12)
    String phone;

    @Schema(description = "Почта",
            example = "email@email.com")
    @JsonProperty(value = "email")
    @Size(max = 100)
    @Email
    String email;

    @Schema(description = "Дата рождения в формате yyyy-MM-dd",
            example = "2000-12-21")
    @JsonProperty(value = "dateOfBirth")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Date of birth must be format yyyy-MM-dd")
    LocalDate dateOfBirth;

    @Schema(description = "ФИО",
            example = "Иванов Иван Иванович")
    @JsonProperty(value = "fullname")
    @Size(max = 100)
    @NotNull(message = "Fullname mustn't null")
    @NotEmpty(message = "Fullname must be filled in")
    String fullname;

    @Schema(description = "Логин",
            example = "login")
    @JsonProperty(value = "login")
    @NotNull(message = "Login mustn't null")
    @Size(max = 100)
    @NotEmpty(message = "Login must be filled in")
    String login;

    @Schema(description = "Пароль",
            example = "1239010")
    @JsonProperty(value = "password")
    @NotNull(message = "Password mustn't null")
    @Size(max = 100)
    @NotEmpty(message = "Password must be filled in")
    String password;

    @Schema(description = "Начальный баланс",
            example = "3")
    @JsonProperty(value = "balance")
    @Min(value = 0, message = "Balance mustn't less 0")
    @NotNull
    BigDecimal balance;

}
