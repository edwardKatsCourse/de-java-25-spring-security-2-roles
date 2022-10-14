package com.example.entity;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class RoleTypeConverter implements AttributeConverter<RoleType, Integer> {
    @Override
    public Integer convertToDatabaseColumn(RoleType roleType) {
        return roleType == null ? null : roleType.getRoleId();
    }

    @Override
    public RoleType convertToEntityAttribute(Integer integer) {
        return integer == null ? null : RoleType.findById(integer);
    }
}
