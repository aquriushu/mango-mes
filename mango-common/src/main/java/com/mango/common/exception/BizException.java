package com.mango.common.exception;

import com.mango.common.constant.ComConst;

/**
 * 用于声明业务异常：如员工已存在，手机号已存在等。日志记录为warn或info，非error。
 * 其他技术异常：如OOM，空指针，代码逻辑错误等，由全局异常拦截器中的Exception兜底
 */
public class BizException extends RuntimeException{

    private int code;      // 可预测的异常，编码使用ComConst.BIZ_EXP_CODE=600
    private String msg;

    public BizException(String msg) {
        super(msg);
        this.code = ComConst.BIZ_EXP_CODE;   // 默认业务异常码
        this.msg = msg;
    }

    public BizException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
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
}
