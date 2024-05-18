package com.effectivemobile.bankoperationsservice.repository;

import com.effectivemobile.bankoperationsservice.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByToken(String token);
    @Query(nativeQuery = true, value = "select * from token where user_id = ?1 and expired = false and revoked = false")
    List<Token> findAllValidTokenByUser(Long id);
}
