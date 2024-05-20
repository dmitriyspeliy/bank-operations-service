package com.effectivemobile.bankoperationsservice.service;

import com.effectivemobile.bankoperationsservice.model.User;
import com.effectivemobile.bankoperationsservice.model.UserAccount;
import com.effectivemobile.bankoperationsservice.repository.UserAccountRepository;
import com.effectivemobile.bankoperationsservice.repository.UserRepository;
import com.effectivemobile.bankoperationsservice.utils.exception.BadRequestException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserAccountServiceTest {
    @InjectMocks
    private UserAccountService accountService;
    @Mock
    private UserAccountRepository userAccountRepository;
    @Mock
    private UserRepository userRepository;

    private UserAccount sender;
    private UserAccount receiver;
    private BigDecimal valueToSend;
    private Long receiverId;
    private Long senderId;
    private User receiverUser;
    private User senderUser;

    @BeforeEach
    void setUp() {
        sender = UserAccount.builder()
                .user(senderUser)
                .currentBalance(BigDecimal.valueOf(10_000L))
                .initBalance(BigDecimal.valueOf(100))
                .build();

        receiver = UserAccount.builder()
                .user(receiverUser)
                .currentBalance(BigDecimal.valueOf(0))
                .initBalance(BigDecimal.valueOf(100))
                .build();
        receiverUser = User.builder()
                .email("receiver@email.com")
                .dateOfBirth(LocalDate.now())
                .phone("89299292812")
                .fullname("ReceiverName")
                .login("receiver")
                .password("receiverPass")
                .userAccount(receiver)
                .build();
        senderUser = User.builder()
                .email("sender@email.com")
                .dateOfBirth(LocalDate.now())
                .phone("89299292811")
                .fullname("SenderName")
                .login("sender")
                .password("senderPass")
                .userAccount(sender)
                .build();


        valueToSend = BigDecimal.ONE;
        receiverId = 1L;
        senderId = 2L;
    }

    @AfterEach
    void tearDown() {
        sender = null;
        receiver = null;
        valueToSend = null;
    }

    @Test
    @DisplayName(value = "Smoke test")
    void unit_smoke_test_when_sender_send_all_money_return_ok() throws ExecutionException, InterruptedException {
        when(userRepository.findById(receiverId)).thenReturn(Optional.ofNullable(receiverUser));
        when(userRepository.findById(senderId)).thenReturn(Optional.ofNullable(senderUser));

        int numberOfThreads = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);

        while (sender.getCurrentBalance().compareTo(BigDecimal.ZERO) != 0){
            executorService.submit(() -> {
                try {
                    accountService.transferMoney(senderId, receiverId, valueToSend);
                } catch (BadRequestException e) {
                    throw new RuntimeException(e);
                }
            }).get();
        }

        assertEquals(BigDecimal.valueOf(10_000L), receiver.getCurrentBalance());
        assertEquals(BigDecimal.ZERO, sender.getCurrentBalance());

    }

    @Test
    @DisplayName(value = "Value balance less than transfer money")
    void unit_test_when_receiver_send_money_return_400() {
        sender.setCurrentBalance(BigDecimal.valueOf(500));
        when(userRepository.findById(receiverId)).thenReturn(Optional.ofNullable(receiverUser));
        when(userRepository.findById(senderId)).thenReturn(Optional.ofNullable(senderUser));

        Assertions.assertThrows(BadRequestException.class, () -> {
            accountService.transferMoney(senderId, receiverId, BigDecimal.valueOf(10_001L));
        });

    }

    @Test
    @DisplayName(value = "Sender or receiver doesn't exists")
    void unit_test_when_receiver_send_money_return_bad_request() {
        when(userRepository.findById(receiverId)).thenReturn(Optional.empty());
        when(userRepository.findById(senderId)).thenReturn(Optional.ofNullable(senderUser));

        Assertions.assertThrows(BadRequestException.class, () -> {
            accountService.transferMoney(senderId, receiverId, BigDecimal.valueOf(1000L));
        });

    }

    @Test
    @DisplayName(value = "Check invalid param. Null param")
    void unit_test_when_take_invalid_param_return_bad_request1() {
        Assertions.assertThrows(BadRequestException.class, () -> {
            accountService.transferMoney(null, receiverId, BigDecimal.valueOf(1000L));
        });
        Assertions.assertThrows(BadRequestException.class, () -> {
            accountService.transferMoney(senderId, null, BigDecimal.valueOf(1000L));
        });
        Assertions.assertThrows(BadRequestException.class, () -> {
            accountService.transferMoney(senderId, receiverId, null);
        });
    }

    @Test
    @DisplayName(value = "Check invalid param. Transfer money less than 0 or equals 0")
    void unit_test_when_take_invalid_param_return_bad_request2() {
        Assertions.assertThrows(BadRequestException.class, () -> {
            accountService.transferMoney(senderId, receiverId, BigDecimal.valueOf(-1));
        });
        Assertions.assertThrows(BadRequestException.class, () -> {
            accountService.transferMoney(senderId, receiverId, BigDecimal.valueOf(0));
        });
    }
}