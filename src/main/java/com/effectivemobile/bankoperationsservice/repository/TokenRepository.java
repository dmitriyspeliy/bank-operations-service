package com.effectivemobile.bankoperationsservice.repository;

import com.effectivemobile.bankoperationsservice.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {
}
