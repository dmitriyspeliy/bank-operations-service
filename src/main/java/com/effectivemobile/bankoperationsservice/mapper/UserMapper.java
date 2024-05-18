package com.effectivemobile.bankoperationsservice.mapper;

import com.effectivemobile.bankoperationsservice.dto.UserDto;
import com.effectivemobile.bankoperationsservice.dto.UserInfo;
import com.effectivemobile.bankoperationsservice.model.User;

import static com.effectivemobile.bankoperationsservice.utils.UtilsMethods.passwordEncoder;

public class UserMapper {

    public static User toEntity(UserInfo userInfo) {
        return User.builder()
                .email(userInfo.getEmail())
                .login(userInfo.getLogin())
                .fullname(userInfo.getFullname())
                .password(passwordEncoder().encode(userInfo.getPassword()))
                .dateOfBirth(userInfo.getDateOfBirth())
                .phone(userInfo.getPhone())
                .build();
    }

    public static UserDto toDto(User user) {
        return UserDto.builder()
                .email(user.getEmail())
                .login(user.getLogin())
                .fullname(user.getFullname())
                .dateOfBirth(user.getDateOfBirth())
                .phone(user.getPhone())
                .balance(user.getUserAccount().getCurrentBalance())
                .build();
    }

}
