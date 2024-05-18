package com.effectivemobile.bankoperationsservice.service;

import com.effectivemobile.bankoperationsservice.dto.UserDto;
import com.effectivemobile.bankoperationsservice.dto.UserInfo;
import com.effectivemobile.bankoperationsservice.mapper.UserMapper;
import com.effectivemobile.bankoperationsservice.model.User;
import com.effectivemobile.bankoperationsservice.model.UserAccount;
import com.effectivemobile.bankoperationsservice.repository.UserAccountRepository;
import com.effectivemobile.bankoperationsservice.repository.UserRepository;
import com.effectivemobile.bankoperationsservice.utils.UtilsMethods;
import com.effectivemobile.bankoperationsservice.utils.enums.PostSort;
import com.effectivemobile.bankoperationsservice.utils.exception.BadRequestException;
import com.effectivemobile.bankoperationsservice.utils.exception.ElemNotFound;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.effectivemobile.bankoperationsservice.utils.LogFormatter.logDebug;
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
            var message = String.format("""
                            Can't save user. User exist by param:
                            Login : %s
                            Phone : %s
                            Email : %s""",
                    userInfo.getLogin(), userInfo.getPhone(), userInfo.getEmail());
            logDebug(message);
            throw new BadRequestException(message);
        } else {
            logInfo("Save user in db with login" + userInfo.getLogin());
            User user1 = UserMapper.toEntity(userInfo);
            user = userRepository.save(UserMapper.toEntity(userInfo));
            UserAccount userAccount = UserAccount.builder()
                    .currentBalance(userInfo.getBalance())
                    .initBalance(userInfo.getBalance())
                    .user(user).build();
            userAccountRepository.save(userAccount);
        }
        return user;
    }

    public void changeUserPhone(String phone, Long id) throws ElemNotFound {
        Optional<User> userOptional = userRepository.findByPhone(phone);
        if (userOptional.isPresent()) {
            logDebug("Couldn't change/add phone cause already exists with this phone");
            throw new ElemNotFound("Couldn't change/add phone cause already exists with this phone");
        } else {
            userRepository.updateUserPhone(phone, id);
        }
    }

    public void changeUserEmail(String email, Long id) throws ElemNotFound {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            logDebug("Couldn't change/add email cause already exists with this email");
            throw new ElemNotFound("Couldn't change/add email cause already exists with this email");
        } else {
            userRepository.updateUserEmail(email, id);
        }
    }

    //если правильно понял задание, то удалять можно только что то одно, либо почту либо телефон.
    //Если почта не в значении UNKNOWN, то удаляем телефон
    public void deleteUserPhone(String phone, Long id) throws BadRequestException {
        Optional<User> userOptional = userRepository.findById(id);
        if (!Objects.equals(userOptional.get().getEmail(), "UNKNOWN")) {
            userRepository.updateUserPhone("UNKNOWN", id);
        } else {
            logDebug("Couldn't delete phone cause email wasn't fill. Id is " + id);
            throw new BadRequestException("Couldn't delete phone cause email wasn't fill. ID is " + id);
        }
    }

    //если правильно понял задание, то удалять можно только что то одно, либо почту либо телефон.
    //Если телефон не в значении UNKNOWN, то удаляем почту
    public void deleteUserEmail(Long id) throws BadRequestException {
        Optional<User> userOptional = userRepository.findById(id);
        if (!Objects.equals(userOptional.get().getPhone(), "UNKNOWN")) {
            userRepository.updateUserEmail("UNKNOWN", id);
        } else {
            logDebug("Couldn't delete email cause phone wasn't fill. Id is " + id);
            throw new BadRequestException("Couldn't delete email cause phone wasn't fill. ID is " + id);
        }
    }

    public List<UserDto> findByDateOfBirthAfter(LocalDate localDate, int pageNumber, int limit,
                                                PostSort postSort) {
        Page<User> userPage = userRepository.findByDateOfBirthAfter(localDate, PageRequest.of(pageNumber,
                limit, postSort.getSortValue()));
        return userPage.stream().map(UserMapper::toDto).collect(Collectors.toList());
    }

    public User findByPhone(String phone) throws ElemNotFound {
        Optional<User> userOptional = userRepository.findByPhone(phone);
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            logDebug("Couldn't find user with phone " + phone);
            throw new ElemNotFound("Couldn't find user with phone " + phone);
        }
    }

    public User findByEmail(String email) throws ElemNotFound {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            logDebug("Couldn't find user with email " + email);
            throw new ElemNotFound("Couldn't find user with email " + email);
        }
    }

    public List<UserDto> findByFullnameLike(String fullname, int pageNumber, int limit,
                                            PostSort postSort) {
        Page<User> userPage = userRepository.findByFullnameLike(fullname, PageRequest.of(pageNumber,
                limit, postSort.getSortValue()));
        return userPage.stream().map(UserMapper::toDto).collect(Collectors.toList());
    }


}
