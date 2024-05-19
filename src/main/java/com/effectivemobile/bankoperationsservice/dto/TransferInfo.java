package com.effectivemobile.bankoperationsservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Информация про перевод")
public class TransferInfo {
    @NotNull(message = "Sender id mustn't null")
    @Min(value = 1, message = "Sender id mustn't less 1")
    @Schema(description = "Идентификатор отправителя",
            example = "1")
    Long sender;
    @NotNull(message = "Receiver id mustn't null")
    @Min(value = 1, message = "Receiver id mustn't less 1")
    @Schema(description = "Идентификатор получателя",
            example = "2")
    Long receiver;
    @NotNull(message = "Fullname mustn't null")
    @NotEmpty(message = "Fullname must be filled in")
    @Schema(description = "Сумма перевода",
            example = "100")
    BigDecimal value;
}
