package com.vidya.assignment.employee.details.processing.domain.util;

import com.vidya.assignment.employee.details.processing.domain.mongo.entity.EmployeeMongo;
import com.vidya.assignment.employee.details.processing.domain.dto.EmployeeDTO;
import com.vidya.assignment.employee.details.processing.domain.postgres.entity.Employee;

public class EntityMapper {

    public static EmployeeDTO mapEmployeeToDTO(Employee employee) {
        return EmployeeDTO.builder()
                .id(employee.getId()!=null?employee.getId().toString():null)
                .employeeId(employee.getEmployeeId())
                .dateOfBirth(employee.getDateOfBirth())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .employeeId(employee.getEmployeeId())
                .email(employee.getEmail())
                .phoneNo(employee.getPhoneNo())
                .build();
    }

    public static Employee mapEmployeeFromDTO(EmployeeDTO employee) {
        return Employee.builder()
                .id(employee.getId()!= null ? Long.parseLong(employee.getId()):null)
                .employeeId(employee.getEmployeeId())
                .dateOfBirth(employee.getDateOfBirth())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .phoneNo(employee.getPhoneNo())
                .build();
    }
    public static EmployeeDTO mapJsonToEmployeeDTO(EmployeeMongo employee) {
        return EmployeeDTO.builder()
                .id(employee.getId())
                .employeeId(employee.getEmployeeId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .employeeId(employee.getEmployeeId())
                .email(employee.getEmail())
                .phoneNo(employee.getPhoneNo())
                .build();
    }
    public static EmployeeMongo mapDTOtoJsonEmployee(EmployeeDTO employee) {
        return EmployeeMongo.builder()
                .id(employee.getId())
                .employeeId(employee.getEmployeeId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .employeeId(employee.getEmployeeId())
                .email(employee.getEmail())
                .phoneNo(employee.getPhoneNo())
                .build();
    }
}
