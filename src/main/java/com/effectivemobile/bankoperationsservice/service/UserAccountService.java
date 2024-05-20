package com.effectivemobile.bankoperationsservice.service;

import com.effectivemobile.bankoperationsservice.dto.TransferResponse;
import com.effectivemobile.bankoperationsservice.model.User;
import com.effectivemobile.bankoperationsservice.model.UserAccount;
import com.effectivemobile.bankoperationsservice.repository.UserAccountRepository;
import com.effectivemobile.bankoperationsservice.repository.UserRepository;
import com.effectivemobile.bankoperationsservice.utils.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import static com.effectivemobile.bankoperationsservice.utils.LogFormatter.logError;

@Component
@RequiredArgsConstructor
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final UserRepository userRepository;

    @Transactional
    public void increaseDepositLevel(UserAccount userAccount) {
        BigDecimal currentBalance = userAccount.getCurrentBalance();
        BigDecimal initDeposit = userAccount.getInitBalance();

        BigDecimal limit = (initDeposit.divide(BigDecimal.valueOf(100L), 3, RoundingMode.HALF_UP))
                .multiply(BigDecimal.valueOf(207L));

        BigDecimal currentBalanceWith5percent = ((currentBalance.divide(BigDecimal.valueOf(100L),
                3, RoundingMode.HALF_UP))
                .multiply(BigDecimal.valueOf(5L))).add(currentBalance);

        if (currentBalanceWith5percent.compareTo(limit) <= 0) {
            userAccount.setCurrentBalance(currentBalanceWith5percent);
            userAccountRepository.updateUserCurrentBalance(userAccount.getCurrentBalance(), userAccount.getId());
        }
    }

    @Transactional(propagation = Propagation.NEVER)
    public synchronized TransferResponse transferMoney(Long sender, Long receiver, BigDecimal transferValue) throws BadRequestException {
        checkParam(sender, receiver, transferValue);
        Optional<User> senderAccountOptional = userRepository.findById(sender);
        Optional<User> receiverAccountOptional = userRepository.findById(receiver);
        if (senderAccountOptional.isPresent() && receiverAccountOptional.isPresent()) {
            UserAccount senderAccount = senderAccountOptional.get().getUserAccount();
            UserAccount receiverAccount = receiverAccountOptional.get().getUserAccount();
            BigDecimal currentBalanceSender = senderAccount.getCurrentBalance();
            BigDecimal currentBalanceReceiver = receiverAccount.getCurrentBalance();
            if (currentBalanceSender.compareTo(transferValue) < 0) {
                logError("Value balance less than transfer money");
                throw new BadRequestException("Value balance less than transfer money");
            } else {
                senderAccount.setCurrentBalance(currentBalanceSender.subtract(transferValue));
                receiverAccount.setCurrentBalance(currentBalanceReceiver.add(transferValue));
                userAccountRepository.save(senderAccount);
                userAccountRepository.save(receiverAccount);
                return TransferResponse.builder().status("OK").build();
            }
        } else {
            logError("Sender or receiver doesn't exists");
            throw new BadRequestException("Sender or receiver doesn't exists");
        }
    }

    private void checkParam(Long sender, Long receiver, BigDecimal transferValue) throws BadRequestException {
        if (sender == null || receiver == null) {
            throw new BadRequestException("SenderId or receiverId mustn't null");
        }
        if (transferValue == null || transferValue.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Transfer money must be more than zero");
        }
    }
}
