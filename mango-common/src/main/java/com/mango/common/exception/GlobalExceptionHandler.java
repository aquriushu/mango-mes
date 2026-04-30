package com.mango.common.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.mango.common.domain.ApiResult;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理 @Valid 参数校验失败（RequestBody 校验）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResult<Map<String, List<String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // 1. 收集所有字段的错误信息
        Map<String, List<String>> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.computeIfAbsent(error.getField(), k -> new ArrayList<>()).add(error.getDefaultMessage());
        });

        // 2，使用自己定义的ApiResult返回参数校验
        ApiResult<Map<String, List<String>>> apiResult = ApiResult.fail("参数校验失败！");
        apiResult.setData(errors);

        // 3. 返回 400 Bad Request 状态码和自定义的错误信息
        return apiResult;
    }

    /**
     * 处理单个参数校验失败（如 @RequestParam @Min 等）
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ApiResult<String> handleConstraintViolation(ConstraintViolationException ex) {
        String message = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("; "));
//        log.warn("单个参数校验失败: {}", message);
        return ApiResult.fail(message);
    }

    /**
     * 处理时间参数的转换失败
     *
     * @param e
     * @return
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ApiResult<String> handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        Throwable cause = e.getCause();

        // todo 下面的时间校验需要验证哪种方式正确
//        if (e.getMessage().contains("LocalDateTime") || e.getMessage().contains("DateTimeParseException")) {
//            return ApiResult.fail("时间格式错误，请使用 yyyy-MM-dd HH:mm:ss 格式");
//        }

//        // 1. 处理时间格式错误
//        if (cause instanceof DateTimeParseException) {
//            return ApiResult.fail("时间格式错误，请使用 yyyy-MM-dd HH:mm:ss 格式");
//        }

        // 2. 处理 Boolean 类型错误
        if (cause instanceof InvalidFormatException) {
            InvalidFormatException ife = (InvalidFormatException) cause;
            // 判断目标类型是否为 Boolean
            if (ife.getTargetType() == Boolean.class) {
                return ApiResult.fail("启用/禁用状态值只能为 true 或 false，请检查输入");
            }
            // 其他类型转换错误，例如数字、枚举等（可选）
            return ApiResult.fail("字段 " + ife.getPathReference() + " 格式错误，期望类型 " + ife.getTargetType().getSimpleName());
        }
        return ApiResult.fail("请求参数格式错误: " + e.getMessage());
    }

    /**
     * 业务异常，如：员工已存在等，日志记录为warn
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(BizException.class)
    public ApiResult<Void> handleBusiness(BizException ex) {
//        log.warn("业务异常: {}", ex.getMessage());
        // 可以使用 ex.getCode() 获取自定义错误码
        return ApiResult.fail(ex.getCode(), ex.getMessage());
    }

    /**
     * 处理技术异常（兜底）
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ApiResult<Void> handleUnknown(Exception ex) {
//        log.error("系统未知异常: ", ex);  // 打印完整堆栈
        ex.printStackTrace();
        return ApiResult.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), "系统繁忙，请稍后重试");
    }

}
