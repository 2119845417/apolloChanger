package com.ncf.apollodemo.enums;

public enum RoleEnum {
    PM(1,"产品"),

    CODER(2,"开发者"),

    LEADER(3,"负责人");

    private Integer id;
    private String name;

    RoleEnum(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() { return name; }
}
