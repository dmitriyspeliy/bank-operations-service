package com.effectivemobile.bankoperationsservice.repository;

import com.effectivemobile.bankoperationsservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLogin(String login);

    @Query(nativeQuery = true, value = "select * from \"user\" where login = ?1 or email = ?2 or phone = ?3")
    Optional<User> findByLoginEmailPhone(String login, String email, String phone);

}
