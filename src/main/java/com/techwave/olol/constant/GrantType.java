package com.techwave.olol.constant;

import lombok.Getter;

@Getter
public enum GrantType {
    CUSTOM("custom"),
    KAKAO("kakao");

    private String name;

    GrantType(String name) {
        this.name = name;
    }
}
