package com.incodegroup.test.exeption;

import com.incodegroup.test.enumeration.SystemErrorCode;
import com.incodegroup.test.exeption.dto.ApiErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice
public class ExceptionTranslator {

    @ExceptionHandler(value = SystemException.class)
    public ResponseEntity<ApiErrorDto> handleSystemException(final SystemException e, final HttpServletRequest request) {
        log.warn(e.getClass().getSimpleName(), e);
        final SystemErrorCode systemErrorCode = e.getErrorCode();
        final int codeValue = systemErrorCode.getValue();
        final HttpStatus status = HttpStatus.valueOf(codeValue);
        final ApiErrorDto apiErrorDto = new ApiErrorDto(e, status, request);
        return ResponseEntity.status(apiErrorDto.getStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(apiErrorDto);
    }
}
