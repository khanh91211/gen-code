package com.fw.core.config;

import com.fw.core.util.RequestUtil;
import com.fw.model.dto.base.ErrorResponse;
import com.fw.model.dto.exception.BusinessException;
import com.fw.model.enumeration.ErrorEnum;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author khanhtd
 * @Date 2022
 */
@ControllerAdvice
public class ExceptionConfig {
    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE)
    protected ErrorResponse handleBlException(BusinessException ex) {
        ErrorEnum err = ex.getError();
        String code = err.getCode();
        String msg = getMessage(err.getMessageId());
        return RequestUtil.err(code, msg, ex.getAdditionalData());
    }

    @ExceptionHandler({HttpClientErrorException.Unauthorized.class, AccessDeniedException.class})
    @ResponseBody
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    protected ErrorResponse handleUnauthorized(Exception ex) {
        return RequestUtil.err("401", ex.getMessage(), null);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE)
    protected ErrorResponse handleUnauthorized(RuntimeException ex) {
        return RequestUtil.err("500", ex.getMessage(), null);
    }

    @ExceptionHandler(Throwable.class)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE)
    protected ErrorResponse handleOtherThrowable(Throwable ex) {
        return RequestUtil.err(ex.getClass().getCanonicalName(), ex.getMessage(), null);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ErrorResponse handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> message = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String detailMessage = fieldName.trim() + ":: " + error.getDefaultMessage();
            message.add(detailMessage);
        });
        return RequestUtil.err("400", message.toString(), "");
    }

    private static final String BUNDLE_NAME = "messages-domain"; // Tên file properties (bỏ đuôi .properties)
    private static final ResourceBundle resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME);

    public String getMessage(String key) {
        try {
            return resourceBundle.getString(key);
        } catch (Exception ex) {
            ex.printStackTrace();
            return key;
        }
    }
}
