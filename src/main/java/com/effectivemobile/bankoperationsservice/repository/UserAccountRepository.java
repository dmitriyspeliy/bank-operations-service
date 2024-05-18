package com.effectivemobile.bankoperationsservice.repository;

import com.effectivemobile.bankoperationsservice.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;


public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    @Query(nativeQuery = true, value = "select * from user_account where user_id = ?1")
    Optional<UserAccount> findByUser(Long id);

    @Modifying
    @Query("update UserAccount u set u.currentBalance = :currentBalance where u.id = :id")
    void updateUserCurrentBalance(@Param(value = "currentBalance") BigDecimal currentBalance, @Param(value = "id") Long id);
}
