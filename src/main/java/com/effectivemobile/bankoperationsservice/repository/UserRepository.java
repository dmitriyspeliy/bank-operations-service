package com.effectivemobile.bankoperationsservice.repository;

import com.effectivemobile.bankoperationsservice.model.User;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLogin(String login);

    Optional<User> findByPhone(String phone);

    Optional<User> findByEmail(String email);

    @Query(nativeQuery = true, value = "select * from \"user\" where login = ?1 or email = ?2 or phone = ?3")
    Optional<User> findByLoginEmailPhone(String login, String email, String phone);

    @Modifying
    @Transactional
    @Query("update User u set u.phone = :phone where u.id = :id")
    void updateUserPhone(@Param(value = "phone") String phone, @Param(value = "id") Long id);

    @Modifying
    @Transactional
    @Query("update User u set u.email = :email where u.id = :id")
    void updateUserEmail(@Param(value = "email") String email, @Param(value = "id") Long id);

    @Query(nativeQuery = true, value = "select * from \"user\" where date_of_birth > ?1")
    Page<User> findByDateOfBirthAfter(LocalDate dateOfBirth, Pageable pageable);

    @Query(nativeQuery = true, value = "select * from \"user\" where fullname like ?1||'%'")
    Page<User> findByFullnameLike(String fullname, Pageable pageable);


}
