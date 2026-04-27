package com.mango.common.domain;

import com.mango.common.constant.ComConst;

import java.io.Serializable;

public class ApiResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;


    private int code;

    private String msg;

    private T data;

    private boolean isSuccess;

    public static <T> ApiResult<T> success() {
        return restResult(null, ComConst.SUCCESS, null);
    }

    public static <T> ApiResult<T> success(T data) {
        return restResult(data, ComConst.SUCCESS, null);
    }

    public static <T> ApiResult<T> success(String msg) {
        return restResult(null, ComConst.SUCCESS, msg);
    }

    public static <T> ApiResult<T> success(T data, String msg) {
        return restResult(data, ComConst.SUCCESS, msg);
    }

    public static <T> ApiResult<T> fail() {
        return restResult(null, ComConst.FAIL, null);
    }

    public static <T> ApiResult<T> fail(String msg) {
        return restResult(null, ComConst.FAIL, msg);
    }

    public static <T> ApiResult<T> fail(T data) {
        return restResult(data, ComConst.FAIL, null);
    }

    public static <T> ApiResult<T> fail(T data, String msg) {
        return restResult(data, ComConst.FAIL, msg);
    }

    public static <T> ApiResult<T> fail(int code, String msg) {
        return restResult(null, code, msg);
    }

    private static <T> ApiResult<T> restResult(T data, int code, String msg) {
        ApiResult<T> apiResult = new ApiResult<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMsg(msg);
        apiResult.setSuccess(apiResult.getCode()==ComConst.SUCCESS);
        return apiResult;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

}
