package com.effectivemobile.bankoperationsservice.controller;

import com.effectivemobile.bankoperationsservice.dto.UserDto;
import com.effectivemobile.bankoperationsservice.service.UserService;
import com.effectivemobile.bankoperationsservice.utils.enums.PostSort;
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
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/searching")
@RequiredArgsConstructor
@Validated
@Tag(name = "Контролер для поиска пользователей", description = "Поиск по почте, телефону, и тд. С сортировкой и пагинацией")
public class SearchingController {

    private final UserService userService;

    @Operation(summary = "Поиск юзеров по дате",
            description = "В списке будут пользователи, чья дата больше переданной")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Получили список",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UserDto.class)))}),
            @ApiResponse(
                    responseCode = "5XX",
                    description = "Не удалось получить ответ. Причина в сообщении",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))})})
    @GetMapping("/date-of-birth")
    public List<UserDto> filterByDateOfBirth(
            @Parameter(description = "Date of birth must be format yyyy-MM-dd", example = "2012-12-29")
            @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Date of birth must be format yyyy-MM-dd")
            @RequestParam("date") LocalDate localDate,
            @Parameter(description = "Number of page") @Min(0) @RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
            @Parameter(description = "Limit elem on page") @Min(1) @Max(100) @RequestParam("limit") int limit,
            @Parameter(description = "Sorting param") @NotNull @RequestParam("sort") PostSort sort
    ) {
        return userService.findByDateOfBirthAfter(localDate, pageNumber, limit, sort);
    }

    @Operation(summary = "Поиск юзера по телефону",
            description = "Найти юзера по телефону")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Получили юзера",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UserDto.class)))}),
            @ApiResponse(
                    responseCode = "400",
                    description = "Нет юзера по такому телефону",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))}),
            @ApiResponse(
                    responseCode = "5XX",
                    description = "Не удалось получить ответ. Причина в сообщении",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))})})
    @GetMapping("/phone")
    public ResponseEntity<UserDto> findUserByPhone(@RequestParam("phone") @Parameter(description = "Phone") @Size(max = 12) String phone)
            throws ElemNotFound {
        return ResponseEntity.ok(userService.findByPhone(phone));
    }

    @Operation(summary = "Поиск юзера по почте",
            description = "Найти юзера по почте")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Получили юзера",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UserDto.class)))}),
            @ApiResponse(
                    responseCode = "400",
                    description = "Нет юзера по такой почте",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))}),
            @ApiResponse(
                    responseCode = "5XX",
                    description = "Не удалось получить ответ. Причина в сообщении",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))})})
    @GetMapping("/email")
    public ResponseEntity<UserDto> findUserByEmail(@Parameter(description = "Email") @Size(max = 100) @RequestParam(name = "email") String email)
            throws ElemNotFound {
        return ResponseEntity.ok(userService.findByEmail(email));
    }

    @Operation(summary = "Поиск юзера по полному ФИО",
            description = "Найти юзера по полному ФИО")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Получили список юзеров",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UserDto.class)))}),
            @ApiResponse(
                    responseCode = "5XX",
                    description = "Не удалось получить ответ. Причина в сообщении",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))})})
    @GetMapping("/fullname")
    public List<UserDto> findUserByFullname(@Parameter(description = "Full name")
                                            @Size(max = 100)
                                            @NotNull(message = "Fullname mustn't null")
                                            @NotEmpty(message = "Fullname must be filled in")
                                            @RequestParam("fullname") String fullname,
                                            @Parameter(description = "Number of page") @Min(0) @RequestParam(name = "pageNumber", defaultValue = "0")
                                            int pageNumber,
                                            @Parameter(description = "Limit elem on page") @Min(1) @Max(100) @RequestParam("limit") int limit,
                                            @Parameter(description = "Sorting param") @NotNull @RequestParam("sort") PostSort sort
    ) {
        return userService.findByFullnameLike(fullname, pageNumber, limit, sort);
    }

}
