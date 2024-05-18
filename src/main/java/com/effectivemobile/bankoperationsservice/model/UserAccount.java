package com.effectivemobile.bankoperationsservice.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(name = "user_account")
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuperBuilder
@NoArgsConstructor
public class UserAccount extends AbstractEntity {
    @Column(name = "init_balance")
    BigDecimal initBalance;
    @Column(name = "current_balance")
    BigDecimal currentBalance;
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    User user;
}
