package com.ncf.apollodemo.utils;

import com.ncf.apollodemo.pojo.dto.CallBackDTO;

import java.util.HashMap;
import java.util.Map;

public class SingletonMap {
    private static final Map<String, CallBackDTO> instance = new HashMap<>();
    private SingletonMap() {}
    public static Map<String, CallBackDTO> getInstance() {
        return instance;
    }
}