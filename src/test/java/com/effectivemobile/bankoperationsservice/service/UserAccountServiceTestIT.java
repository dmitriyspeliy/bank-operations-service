package com.effectivemobile.bankoperationsservice.service;

import com.effectivemobile.bankoperationsservice.BankOperationsServiceApplicationTests;
import com.effectivemobile.bankoperationsservice.model.User;
import com.effectivemobile.bankoperationsservice.model.UserAccount;
import com.effectivemobile.bankoperationsservice.repository.UserAccountRepository;
import com.effectivemobile.bankoperationsservice.repository.UserRepository;
import com.effectivemobile.bankoperationsservice.utils.LogFormatter;
import com.effectivemobile.bankoperationsservice.utils.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;


@RequiredArgsConstructor
class UserAccountServiceTestIT extends BankOperationsServiceApplicationTests {
    private BigDecimal valueToSend;
    private User receiverUser;
    private User senderUser;

    @Autowired
    private UserAccountRepository userAccountRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserAccountService accountService;


    @BeforeEach
    void setUp() {
        valueToSend = BigDecimal.ONE;
        receiverUser = User.builder()
                .email("receiver@email.com")
                .dateOfBirth(LocalDate.now())
                .phone("89299292812")
                .fullname("ReceiverName")
                .login("receiver")
                .password("receiverPass")
                .build();
        senderUser = User.builder()
                .email("sender@email.com")
                .dateOfBirth(LocalDate.now())
                .phone("89299292811")
                .fullname("SenderName")
                .login("sender")
                .password("senderPass")
                .build();
        UserAccount receiverAccount = UserAccount.builder()
                .currentBalance(BigDecimal.valueOf(0))
                .initBalance(BigDecimal.valueOf(0))
                .user(receiverUser)
                .build();
        UserAccount senderAccount = UserAccount.builder()
                .currentBalance(BigDecimal.valueOf(100L))
                .initBalance(BigDecimal.valueOf(100L))
                .user(senderUser)
                .build();
        userRepository.save(receiverUser);
        userRepository.save(senderUser);
        userAccountRepository.save(receiverAccount);
        userAccountRepository.save(senderAccount);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        userAccountRepository.deleteAll();
    }

    @Test
    @DisplayName(value = "Transfer money with parralel thread")
    void when_transfer_money_from_sender_to_receiver_in_parralel_thread_check_account_value_return_ok() {
        IntStream.range(0, 100)
                .parallel()
                .forEach(i -> {
                    try {
                        accountService.transferMoney(senderUser.getId(), receiverUser.getId(), valueToSend);
                    } catch (BadRequestException e) {
                        LogFormatter.logError(e.getMessage());
                    }
                });
        assertEquals(BigDecimal.valueOf(100L), userAccountRepository.findByUser(receiverUser.getId()).get().getCurrentBalance());
        assertEquals(BigDecimal.valueOf(0L), userAccountRepository.findByUser(senderUser.getId()).get().getCurrentBalance());
    }

    @Test
    @DisplayName(value = "Balance less than transfer money")
    void when_transfer_money_from_sender_to_receiver_in_parralel_thread_check_account_value_return_exception1() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            IntStream.range(0, 103)
                    .parallel()
                    .forEach(i -> {
                        try {
                            accountService.transferMoney(senderUser.getId(), receiverUser.getId(), valueToSend);
                        } catch (BadRequestException e) {
                            throw new RuntimeException(e);
                        }
                    });
        });
    }

    @Test
    @DisplayName(value = "Sender or receiver doesn't exists")
    void when_transfer_money_from_sender_to_receiver_in_parralel_thread_check_account_value_return_exception2() {
        userRepository.deleteById(senderUser.getId());
        Assertions.assertThrows(RuntimeException.class, () -> {
            IntStream.range(0, 100)
                    .parallel()
                    .forEach(i -> {
                        try {
                            accountService.transferMoney(senderUser.getId(), receiverUser.getId(), valueToSend);
                        } catch (BadRequestException e) {
                            throw new RuntimeException(e);
                        }
                    });
        });
    }


}