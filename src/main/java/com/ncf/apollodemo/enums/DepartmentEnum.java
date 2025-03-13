package com.ncf.apollodemo.enums;

public enum DepartmentEnum {
    chanpin(1,"产品"),

    kaifa(2,"开发"),

    yewu(3,"业务");

    private Integer id;
    private String name;

    DepartmentEnum(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() { return name; }
}
