package com.effectivemobile.bankoperationsservice.mapper;

import com.effectivemobile.bankoperationsservice.dto.UserInfo;
import com.effectivemobile.bankoperationsservice.model.User;
import com.effectivemobile.bankoperationsservice.model.UserAccount;

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

}
