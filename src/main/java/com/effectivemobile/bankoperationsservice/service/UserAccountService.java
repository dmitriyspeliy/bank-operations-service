package com.effectivemobile.bankoperationsservice.service;

import com.effectivemobile.bankoperationsservice.model.UserAccount;
import com.effectivemobile.bankoperationsservice.repository.UserAccountRepository;
import com.effectivemobile.bankoperationsservice.utils.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;

    @Transactional(isolation = Isolation.REPEATABLE_READ)
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

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void transferMoney(Long sender, Long receiver, BigDecimal transferValue) throws BadRequestException {
        Optional<UserAccount> senderAccountOptional = userAccountRepository.findByUser(sender);
        Optional<UserAccount> receiverAccountOptional = userAccountRepository.findByUser(receiver);
        if (senderAccountOptional.isPresent() && receiverAccountOptional.isPresent()) {
            UserAccount senderAccount = senderAccountOptional.get();
            UserAccount receiverAccount = receiverAccountOptional.get();
            BigDecimal currentBalanceSender = senderAccount.getCurrentBalance();
            BigDecimal currentBalanceReceiver = receiverAccount.getCurrentBalance();
            if (currentBalanceSender.compareTo(transferValue) < 0) {
                throw new BadRequestException("Value balance less than transfer money");
            } else {
                senderAccount.setCurrentBalance(currentBalanceSender.subtract(transferValue));
                receiverAccount.setCurrentBalance(currentBalanceReceiver.add(transferValue));
                userAccountRepository.save(senderAccount);
                userAccountRepository.save(receiverAccount);
            }
        } else {
            throw new BadRequestException("Sender or receiver doesn't exists");
        }
    }
}
