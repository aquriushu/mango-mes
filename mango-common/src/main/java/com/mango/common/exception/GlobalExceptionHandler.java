package com.mango.common.exception;

import com.mango.common.domain.ApiResult;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理 @Valid 参数校验失败（RequestBody 校验）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResult<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // 1. 收集所有字段的错误信息
        Map<String, Object> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });


        // 2，使用自己定义的ApiResult返回参数校验
        ApiResult<Map<String, Object>> apiResult = ApiResult.fail("参数校验失败！");
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

}
