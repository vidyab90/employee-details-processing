package com.vidya.assignment.employee.details.processing.domain.postgres.service;

import com.vidya.assignment.employee.details.processing.domain.dto.EmployeeDTO;
import com.vidya.assignment.employee.details.processing.domain.postgres.entity.Employee;
import com.vidya.assignment.employee.details.processing.domain.postgres.repo.EmployeeRepo;
import com.vidya.assignment.employee.details.processing.domain.util.EntityMapper;
import com.vidya.assignment.employee.details.processing.exception.ServiceException;
import com.vidya.assignment.employee.details.processing.model.ErrorCode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@DataJpaTest
@Import(DbEmployeeService.class)
class DbEmployeeServiceTest {

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private DbEmployeeService dbEmployeeService;

    private EmployeeDTO employeeDTO;
    private Employee employee;
    private List<EmployeeDTO> employeeDTOList;

    @BeforeEach
    void setUp() {
        employee = Employee.builder()
                .employeeId("ABC123")
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(LocalDate.of(1980, 1, 1))
                .email("jone@gmail.com")
                .phoneNo("123456789")
                .build();
        employee = employeeRepo.saveAndFlush(employee);
        employeeDTO = EntityMapper.mapEmployeeToDTO(employee);
        //employeeDTOList.add(employeeDTO);

        employeeDTOList = new ArrayList<>();
        employeeDTOList.add(employeeDTO);

    }

    @AfterEach
    void tearDown() {
        employeeRepo.deleteAll();
        employeeRepo.flush();
    }

    @Test
    void get_exist() {
        Assertions.assertEquals(employeeDTO, dbEmployeeService.getByEmployeeId(employee.getEmployeeId()));
    }

    @Test
    void get_not_exist() {
        RuntimeException actualMessage = Assertions.assertThrows(ServiceException.class, () -> {
            dbEmployeeService.getByEmployeeId("ABC1234");
        });
        ServiceException exException = new ServiceException(ErrorCode.EMPLOYEE_NOT_FOUND, "Employee not found");
        Assertions.assertEquals(exException.getMessage(), actualMessage.getMessage());

    }

    @Test
    void create_employee_exist() {
        RuntimeException actualMessage = Assertions.assertThrows(ServiceException.class, () -> {
            dbEmployeeService.create(employeeDTO);
        });
        ServiceException exException = new ServiceException(ErrorCode.EMPLOYEE_ALREADY_EXISTS, "Employee already exists");
        Assertions.assertEquals(exException.getMessage(), actualMessage.getMessage());
    }

    @Test
    void create_employee_employeeId_exist() {
        employeeDTO.setId(null);
        RuntimeException actualMessage = Assertions.assertThrows(ServiceException.class, () -> {
            dbEmployeeService.create(employeeDTO);
        });

        ServiceException exException = new ServiceException(ErrorCode.EMPLOYEE_ALREADY_EXISTS, "Employee already exists");
        Assertions.assertEquals(exException.getMessage(), actualMessage.getMessage());
    }

    @Test
    void create_employee_not_exist() {

        employeeDTO = EmployeeDTO.builder()
                .employeeId("ABC125")
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(LocalDate.of(1980, 1, 1))
                .email("jone@gmail.com")
                .phoneNo("123456789")
                .build();

        Assertions.assertEquals(employeeDTO.getEmployeeId(), dbEmployeeService.create(employeeDTO).getEmployeeId());
    }


    @Test
    void update_employee_not_exist() {

        employeeDTO.setEmployeeId("2000L");
        RuntimeException actualMessage = Assertions.assertThrows(ServiceException.class, () -> {
            dbEmployeeService.update(employeeDTO.getEmployeeId(), employeeDTO);
        });
        ServiceException exException = new ServiceException(ErrorCode.EMPLOYEE_NOT_FOUND, "Employee not found");
        Assertions.assertEquals(exException.getMessage(), actualMessage.getMessage());

    }

    @Test
    void update_employee() {
        Assertions.assertEquals(employeeDTO, dbEmployeeService.update(employeeDTO.getEmployeeId(), employeeDTO));
    }

    @Test
    void getAll_exist() {
        Assertions.assertEquals(employeeDTOList, dbEmployeeService.getAll());
    }

    @Test
    void getAll_not_exist() {
        employeeRepo.deleteAll();
        RuntimeException actualMessage = Assertions.assertThrows(ServiceException.class, () -> {
            dbEmployeeService.update(employeeDTO.getEmployeeId(), employeeDTO);
        });
        ServiceException exException = new ServiceException(ErrorCode.EMPLOYEE_NOT_FOUND, "Employee not found");
        Assertions.assertEquals(exException.getMessage(), actualMessage.getMessage());

    }

    @Test
    void delete_not_exist() {
        employeeDTO.setEmployeeId("2000L");
        RuntimeException actualMessage = Assertions.assertThrows(ServiceException.class, () -> {
            dbEmployeeService.update(employeeDTO.getEmployeeId(), employeeDTO);
        });
        ServiceException exException = new ServiceException(ErrorCode.EMPLOYEE_NOT_FOUND, "Employee not found");
        Assertions.assertEquals(exException.getMessage(), actualMessage.getMessage());
    }

    @Test
    void delete_employee_exist() {
        Assertions.assertEquals("Employee with id " + employee.getEmployeeId() + " deleted",
                dbEmployeeService.delete(employee.getEmployeeId()));
    }
}