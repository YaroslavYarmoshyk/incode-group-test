package com.incodegroup.test.exeption.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import javax.servlet.http.HttpServletRequest;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import static com.incodegroup.test.util.DateTimeUtils.DEFAULT_ZONE_ID;
import static com.incodegroup.test.util.DateTimeUtils.ERROR_DATE_FORMAT_PATTERN;


@Data
public class ApiErrorDto {
    @JsonFormat(pattern = ERROR_DATE_FORMAT_PATTERN)
    private Date timestamp;
    private int status;
    private String error;
    private String message;
    private String path;

    public ApiErrorDto(final Exception exception, final HttpStatus status, final HttpServletRequest request) {

        this.timestamp = Date.from(LocalDateTime.now(DEFAULT_ZONE_ID).toInstant(ZoneOffset.UTC));
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.message = exception.getMessage();
        this.path = request.getServletPath();
    }
}
