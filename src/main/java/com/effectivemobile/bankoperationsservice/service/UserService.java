package com.effectivemobile.bankoperationsservice.service;

import com.effectivemobile.bankoperationsservice.dto.UserInfo;
import com.effectivemobile.bankoperationsservice.mapper.UserMapper;
import com.effectivemobile.bankoperationsservice.model.User;
import com.effectivemobile.bankoperationsservice.model.UserAccount;
import com.effectivemobile.bankoperationsservice.repository.UserAccountRepository;
import com.effectivemobile.bankoperationsservice.repository.UserRepository;
import com.effectivemobile.bankoperationsservice.utils.UtilsMethods;
import com.effectivemobile.bankoperationsservice.utils.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.effectivemobile.bankoperationsservice.utils.LogFormatter.logError;
import static com.effectivemobile.bankoperationsservice.utils.LogFormatter.logInfo;

@Component
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserAccountRepository userAccountRepository;
    private final UtilsMethods utilsMethods;


    public User createUser(UserInfo userInfo) throws BadRequestException {
        User user;
        if (utilsMethods.existUserByLoginEmailPhone(userInfo.getLogin(), userInfo.getPhone(), userInfo.getEmail())) {
            var message = String.format("Can't save user. User exist by param by param:\nLogin : %s\nPhone : %s\n Email : %s",
                    userInfo.getLogin(), userInfo.getPhone(), userInfo.getEmail());
            logError(message);
            throw new BadRequestException(message);
        } else {
            logInfo("Save user in db with login" + userInfo.getLogin());
            user = userRepository.save(UserMapper.toEntity(userInfo));
            UserAccount userAccount = UserAccount.builder().balance(userInfo.getBalance()).user(user).build();
            userAccountRepository.save(userAccount);
        }
        return user;
    }


}
