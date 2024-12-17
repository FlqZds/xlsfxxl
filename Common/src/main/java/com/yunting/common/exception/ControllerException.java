package com.yunting.common.exception;

import com.yunting.common.results.ResultMessage;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.lettuce.core.RedisConnectionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.EOFException;
import java.security.SignatureException;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;

import static com.yunting.common.results.ResponseEnum.*;

@Slf4j
//@RestControllerAdvice
public class ControllerException {


    @ExceptionHandler({ConstraintViolationException.class, MethodArgumentNotValidException.class})
    public ResultMessage resolveViolationException(Exception ex) {
        StringJoiner messages = new StringJoiner(",");
        if (ex instanceof ConstraintViolationException) {
            Set<ConstraintViolation<?>> violations = ((ConstraintViolationException) ex).getConstraintViolations();
            for (ConstraintViolation<?> violation : violations) {
                messages.add(violation.getMessage());
            }
        } else {
            List<ObjectError> allErrors = ((MethodArgumentNotValidException) ex).getBindingResult().getAllErrors();
            for (ObjectError error : allErrors) {
                messages.add(error.getDefaultMessage());
            }
        }


        return new ResultMessage(PARAMETER_VALIDATION_ERROR, null);
    }

    // 运行时异常
    @ExceptionHandler(AppException.class)
    public ResultMessage AppException(AppException e) {

        log.error("异常信息：" + e);
        return new ResultMessage(e.getCode(),e.getMessage(),null);
    }

    // eof异常
    @ExceptionHandler(EOFException.class)
    public ResultMessage EOF_Exception(EOFException eof) {

        log.error("文件读取结束,已达至末尾");
        log.error("异常信息："+eof.getMessage());
        return new ResultMessage(FILE_READ_END_ERROR, null);
    }

    //    系统的其他异常
    @ExceptionHandler(Exception.class)
    public ResultMessage otherException(Exception e) {

        log.error("异常原因：" + e.getCause());
        log.error("错误信息：" + e.getMessage());
        return new ResultMessage(SYSTEM_ERROR, null);
    }

    //  算数异常
    @ExceptionHandler(ArithmeticException.class)
    public ResultMessage handleArithmeticException(ArithmeticException e) {

        log.error("算数异常");
        log.error("异常信息："+e.getMessage());
        return new ResultMessage(ARITHMETIC_EXCEPTION, null);
    }

    //请求方式不支持
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResultMessage HttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {

        log.error("请求方式不支持");
        log.error("异常信息：" + e.getMessage());
        return new ResultMessage(REQUEST_METHOD_NOT_SUPPORTED, null);
    }

    //请求参数异常
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResultMessage MissingServletRequestParameterException(MissingServletRequestParameterException e) {

        log.error("请求参数缺失导致的异常错误");
        log.error("异常信息：" + e.getMessage());
        return new ResultMessage(MISSING_REQUEST_PARAMETER_EXCEPTION, null);
    }

    //redis连接异常
    @ExceptionHandler({RedisConnectionException.class, RedisConnectionFailureException.class})
    public ResultMessage RedisConnectionException(RedisConnectionException e) {

        log.error("redis连接异常");
        log.error("异常信息："+e.getMessage());
        return new ResultMessage(REDIS_CONNECTION_ERROR, null);
    }

    //jwt异常  伪造令牌的异常
    @ExceptionHandler({MalformedJwtException.class, SignatureException.class})
    public ResultMessage doJwtException(MalformedJwtException ex, SignatureException ex2) {

        log.error("jwt异常  伪造令牌的异常");

        log.error("异常信息：" + ex.getMessage());
        log.error("异常信息：" + ex2.getMessage());
        return new ResultMessage(TOKEN_INVALIDATE, null);
    }

    //令牌超时
    @ExceptionHandler(ExpiredJwtException.class)
    public ResultMessage doException(ExpiredJwtException ex) {

        log.error("令牌超时");

        log.error("异常信息：" + ex.getMessage());
        return new ResultMessage(TOKEN_EXPIRE, null);
    }



}
