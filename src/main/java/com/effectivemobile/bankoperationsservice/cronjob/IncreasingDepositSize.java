package com.effectivemobile.bankoperationsservice.cronjob;

import com.effectivemobile.bankoperationsservice.repository.UserAccountRepository;
import com.effectivemobile.bankoperationsservice.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.effectivemobile.bankoperationsservice.utils.LogFormatter.logInfo;

@Component
@RequiredArgsConstructor
public class IncreasingDepositSize {

    private final UserAccountService userAccountService;
    private final UserAccountRepository userAccountRepository;
    private static final long FIXED = 60_000L; //1 минута

    @Scheduled(fixedRate = FIXED)
    public void checkMessageStatus() {
        logInfo("Scheduler increasing deposit value for each client on 5%");
        userAccountRepository.findAll().forEach(userAccountService::increaseDepositLevel);
        logInfo("Scheduler finish");
    }


}
