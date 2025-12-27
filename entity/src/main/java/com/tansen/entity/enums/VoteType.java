package com.tansen.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum VoteType {
    YES ("YES"),
    NO("NO");
    private String name;
}
