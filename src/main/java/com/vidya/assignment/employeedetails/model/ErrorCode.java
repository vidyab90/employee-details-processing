package com.vidya.assignment.employeedetails.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    RECORD_ALREADY_EXISTS("GEN001", "Record already exists"),
    EMPLOYEE_ALREADY_EXISTS("EMP002", "Employee already exists"),
    EMPLOYEE_NOT_FOUND("EMP003", "Employee not found"),
    INVALID_INPUT_PARAMETER("GEN004", "Invalid input parameter"),
    INVALID_DATA_SOURCE("GEN005", "Invalid data source"),
    ;

    private final String code;

    private final String message;
}
