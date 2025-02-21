package com.ncf.apollodemo.resp;

import lombok.Data;

@Data
public class ResponseResult<T> {

    private Integer code; //状态码

    private String msg; //提示信息

    private T data; //数据

    private long timestamp;//接口请求时间

    public ResponseResult() {
        this.timestamp = System.currentTimeMillis();
    }

    public static <T> ResponseResult<T> success(T data) {
        ResponseResult<T> r = new ResponseResult<>();
        r.setCode(ReturnCode.RC200.getCode());
        r.setMsg(ReturnCode.RC200.getMsg());
        r.setData(data);
        return r;
    }

    public static <T> ResponseResult<T> error(int code, String msg) {
        ResponseResult<T> r = new ResponseResult<>();
        r.setCode(code);
        r.setMsg(msg);
        r.setData(null);
        return r;
    }
}
