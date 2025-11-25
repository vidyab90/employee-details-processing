package com.vidya.assignment.employeedetails.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {

    private String id;

    @NotBlank(message = "Employee Id is required")
    private String employeeId;

    @NotBlank(message="Employee first name is required")
    private String firstName;

    @NotBlank(message="Employee last name is required")
    private String lastName;

    @Past(message = "Date of birth must be a past date")
    private LocalDate dateOfBirth;

    @Email(message = "Invalid email format")
    private String email;

    @Pattern(
            regexp = "^\\+?[1-9]\\d{1,14}$",
            message = "Invalid international phone number"
    )
    private String phoneNo;
}


