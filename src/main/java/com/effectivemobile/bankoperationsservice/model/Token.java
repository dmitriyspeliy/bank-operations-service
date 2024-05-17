package com.effectivemobile.bankoperationsservice.model;

import com.effectivemobile.bankoperationsservice.utils.enums.TokenType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@Table(name = "token")
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuperBuilder
@NoArgsConstructor
public class Token extends AbstractEntity {
    @Column(name = "token")
    String token;
    @Column(name = "token_type")
    @Enumerated(EnumType.STRING)
    TokenType tokenType = TokenType.BEARER;
    @Column(name = "revoked")
    boolean revoked;
    @Column(name = "expired")
    boolean expired;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;
}