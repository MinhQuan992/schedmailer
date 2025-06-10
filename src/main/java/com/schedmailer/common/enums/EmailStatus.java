package com.schedmailer.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EmailStatus {
    PENDING("Pending"),
    SENT("Sent"),
    FAILED("Failed"),
    RETRYING("Retrying");

    private final String status;
}
