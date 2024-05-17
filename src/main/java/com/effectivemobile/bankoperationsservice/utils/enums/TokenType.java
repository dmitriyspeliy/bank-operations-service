package com.effectivemobile.bankoperationsservice.utils.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Тип токена",
        enumAsRef = true,
        contains = String.class
)
public enum TokenType {
    BEARER
}