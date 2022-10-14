package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

@Getter
@AllArgsConstructor
public enum RoleType {

    USER(1),
    ADMIN(2);

    private final Integer roleId;

    public static RoleType findById(Integer id) {
        if (id == null) {
            return null;
        }

        return Arrays.stream(RoleType.values())
                .filter(x -> Objects.equals(x.getRoleId(), id))
                .findFirst()
                .orElse(null);
    }




}
