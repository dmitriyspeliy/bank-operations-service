package com.effectivemobile.bankoperationsservice.utils.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

@Getter
@RequiredArgsConstructor
public enum PostSort {

    ID_ASC(Sort.by(Sort.Direction.ASC, "id")),
    ID_DESC(Sort.by(Sort.Direction.DESC, "id")),
    DATE_OF_BIRTH_ASC(Sort.by(Sort.Direction.ASC, "date_of_birth"));

    private final Sort sortValue;

}