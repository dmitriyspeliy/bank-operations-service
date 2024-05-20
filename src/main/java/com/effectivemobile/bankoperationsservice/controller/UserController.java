package com.effectivemobile.bankoperationsservice.controller;

import com.effectivemobile.bankoperationsservice.dto.TransferInfo;
import com.effectivemobile.bankoperationsservice.dto.TransferResponse;
import com.effectivemobile.bankoperationsservice.service.UserAccountService;
import com.effectivemobile.bankoperationsservice.service.UserService;
import com.effectivemobile.bankoperationsservice.utils.exception.BadRequestException;
import com.effectivemobile.bankoperationsservice.utils.exception.ElemNotFound;
import com.effectivemobile.bankoperationsservice.utils.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Validated
@Tag(name = "Контролер для юзера", description = "Изменение данных, перевод денег")
public class UserController {

    private final UserService userService;
    private final UserAccountService userAccountService;

    @Operation(summary = "Обновить телефон",
            description = "При успешном обновлении у юзера смениться телефон")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Телефон сменили",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema()))}),
            @ApiResponse(
                    responseCode = "404",
                    description = "Такой телефон уже есть",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))}),
            @ApiResponse(
                    responseCode = "5XX",
                    description = "Не удалось получить ответ. Причина в сообщении",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))})})
    @PutMapping("/change-phone")
    public void changeUserPhone(
            @Parameter(description = "New phone") @NotNull @NotBlank @Size(max = 12) @RequestParam("phone") String phone,
            @Parameter(description = "User ID") @NotNull @NotBlank @RequestParam("id") Long id
    ) throws ElemNotFound {
        userService.changeUserPhone(phone, id);
    }

    @Operation(summary = "Удалить телефон",
            description = "При успешном удалении у юзера удалиться телефон")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Телефон удалился",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema()))}),
            @ApiResponse(
                    responseCode = "400",
                    description = "Удаление невозможно, так как нужно можно удалить либо телефон, либо почту",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))}),
            @ApiResponse(
                    responseCode = "5XX",
                    description = "Не удалось получить ответ. Причина в сообщении",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))})})
    @DeleteMapping("/delete-phone")
    public void deleteUserPhone(
            @Parameter(description = "User ID") @NotNull @NotBlank @RequestParam("id") Long id
    ) throws BadRequestException {
        userService.deleteUserPhone(id);
    }

    @Operation(summary = "Обновить почту",
            description = "При успешном обновлении у юзера смениться почта")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Почту сменили",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema()))}),
            @ApiResponse(
                    responseCode = "404",
                    description = "Такая почта уже есть",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))}),
            @ApiResponse(
                    responseCode = "5XX",
                    description = "Не удалось получить ответ. Причина в сообщении",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))})})
    @PutMapping("/change-email")
    public void changeUserEmail(
            @Parameter(description = "New email") @Size(max = 100) @Email @RequestParam("email") String email,
            @Parameter(description = "User ID") @NotNull @NotBlank @RequestParam("id") Long id
    ) throws ElemNotFound {
        userService.changeUserEmail(email, id);
    }

    @Operation(summary = "Удалить почту",
            description = "При успешном удалении у юзера удалиться почта")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Почта удалилась",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema()))}),
            @ApiResponse(
                    responseCode = "400",
                    description = "Удаление невозможно, так как нужно можно удалить либо телефон, либо почту",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))}),
            @ApiResponse(
                    responseCode = "5XX",
                    description = "Не удалось получить ответ. Причина в сообщении",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))})})
    @DeleteMapping("/delete-email")
    public void deleteUserEmail(
            @Parameter(description = "User ID") @NotNull @NotBlank @RequestParam("id") Long id
    ) throws BadRequestException {
        userService.deleteUserEmail(id);
    }

    @Operation(summary = "Перевод денег",
            description = "Перевод денег с одного счета на другой")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Перевод удался",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = TransferResponse.class)))}),
            @ApiResponse(
                    responseCode = "400",
                    description = "Перевод не удался, либо нет таких пользователей, либо сумма перевод больше чем текущая на счету",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))}),
            @ApiResponse(
                    responseCode = "5XX",
                    description = "Не удалось получить ответ. Причина в сообщении",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))})})
    @PostMapping("/transfer")
    public ResponseEntity<TransferResponse> transferMoney(@RequestBody @Valid TransferInfo transferInfo) throws BadRequestException {
        return ResponseEntity.ok(userAccountService.transferMoney(
                transferInfo.getSender(),
                transferInfo.getReceiver(), transferInfo.getValue()));
    }

}

