package com.effectivemobile.bankoperationsservice.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "user")
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuperBuilder
@NoArgsConstructor
public class User extends AbstractEntity {
    @Column(name = "phone")
    String phone;
    @Column(name = "email")
    String email;
    @Column(name = "date_of_birth")
    LocalDate dateOfBirth;
    @Column(name = "fullname")
    String fullname;
    @Column(name = "login")
    String login;
    @Column(name = "password")
    String password;
    @OneToOne(mappedBy = "user")
    UserAccount userAccount;
    @OneToMany(mappedBy = "user")
    List<Token> tokenList;
}
