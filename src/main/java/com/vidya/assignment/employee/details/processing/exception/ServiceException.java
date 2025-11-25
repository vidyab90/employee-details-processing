package com.vidya.assignment.employee.details.processing.exception;

import com.vidya.assignment.employee.details.processing.model.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@AllArgsConstructor
public class ServiceException extends RuntimeException {


    @Getter
    @Setter
    private ErrorCode errorCode;

    public ServiceException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ServiceException(ErrorCode errorCode, String message, Throwable throwable) {
        super(message, throwable);
        this.errorCode = errorCode;
    }
}
