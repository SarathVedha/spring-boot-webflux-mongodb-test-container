package com.vedha.webflux.utill;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EmployeeSortField {

    ID("id"),

    NAME("name"),

    AGE("age");

    private final String fieldValue;
}
