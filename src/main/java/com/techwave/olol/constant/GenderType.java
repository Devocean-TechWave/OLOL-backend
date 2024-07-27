package com.techwave.olol.constant;

import lombok.Getter;

@Getter
public enum GenderType {

    MALE("male"),
    FEMALE("female");

    private String name;

    GenderType(String name) {
        this.name = name;
    }
}
