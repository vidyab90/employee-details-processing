package com.vidya.assignment.employeedetails.domain.mongo.service;

import com.vidya.assignment.employeedetails.domain.dto.EmployeeDTO;
import com.vidya.assignment.employeedetails.domain.mongo.entity.EmployeeMongo;
import com.vidya.assignment.employeedetails.domain.mongo.repo.MongoEmployeeRepo;
import com.vidya.assignment.employeedetails.domain.util.EntityMapper;
import com.vidya.assignment.employeedetails.exception.ServiceException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.mongodb.test.autoconfigure.DataMongoTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataMongoTest
@Import(JsonEmployeeService.class)
class JsonEmployeeServiceTest {

    @Autowired
    private JsonEmployeeService service;

    @Autowired
    private MongoEmployeeRepo mongoEmployeeRepo;

    private EmployeeDTO employee1, employee2;

    private EmployeeMongo employeeMongo1, employeeMongo2;

    @BeforeEach
    void setUp() {

        employee1 = EmployeeDTO.builder()
                .employeeId("ABC123")
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(LocalDate.of(1980, 1, 1))
                .email("jone@gmail.com")
                .phoneNo("123456789")
                .build();

        employeeMongo1 = EntityMapper.mapDTOtoJsonEmployee(employee1);
        mongoEmployeeRepo.save(employeeMongo1);

        employee2 = EmployeeDTO.builder()
                .employeeId("ABC9876")
                .firstName("Muthu")
                .lastName("Kumar")
                .dateOfBirth(LocalDate.of(1985, 4, 20))
                .email("Muthu1234@gmail.com")
                .phoneNo("098798733")
                .build();

        employeeMongo2 = EntityMapper.mapDTOtoJsonEmployee(employee2);
        mongoEmployeeRepo.save(employeeMongo2);

    }

    @AfterEach
    void tearDown() {
        mongoEmployeeRepo.deleteAll();
    }

    @Test
    void create() {
        mongoEmployeeRepo.deleteAll();

        assertEquals(employee1.getEmployeeId(), service.create(employee1).getEmployeeId());

        assertEquals(employee1.getEmployeeId(), mongoEmployeeRepo.findAll().get(0).getEmployeeId());
    }

    @Test
    void create_exception() {
        assertEquals(employee1.getEmployeeId(), mongoEmployeeRepo.findAll().get(0).getEmployeeId());
        assertEquals(employee2.getEmployeeId(), mongoEmployeeRepo.findAll().get(1).getEmployeeId());

        ServiceException exception = assertThrows(ServiceException.class, () -> service.create(employee1));

        assertEquals("Employee already exists", exception.getMessage());

    }

    @Test
    void update() {

        assertEquals(employee1.getEmployeeId(), mongoEmployeeRepo.findAll().get(0).getEmployeeId());
        assertEquals(employee2.getEmployeeId(), mongoEmployeeRepo.findAll().get(1).getEmployeeId());

        employee1.setEmail("updated@email.com");

        assertEquals(employee1.getEmail(), service.update(employee1.getEmployeeId(), employee1).getEmail());

    }

    @Test
    void update_exception() {

        assertEquals(employee1.getEmployeeId(), mongoEmployeeRepo.findAll().get(0).getEmployeeId());
        assertEquals(employee2.getEmployeeId(), mongoEmployeeRepo.findAll().get(1).getEmployeeId());

        ServiceException exception = assertThrows(ServiceException.class, () -> service.update("TEST1234", employee1));

        assertEquals("Employee not found", exception.getMessage());

        assertEquals(employee1.getEmployeeId(), mongoEmployeeRepo.findAll().get(0).getEmployeeId());
        assertEquals(employee2.getEmployeeId(), mongoEmployeeRepo.findAll().get(1).getEmployeeId());

    }

    @Test
    void getAll() {

        assertEquals(employee1.getEmployeeId(), mongoEmployeeRepo.findAll().get(0).getEmployeeId());
        assertEquals(employee2.getEmployeeId(), mongoEmployeeRepo.findAll().get(1).getEmployeeId());

        List<EmployeeDTO> employeeDTOList = service.getAll();

        assertEquals(2, employeeDTOList.size());

        assertEquals(employee1.getEmployeeId(), employeeDTOList.get(0).getEmployeeId());
        assertEquals(employee2.getEmployeeId(), employeeDTOList.get(1).getEmployeeId());
    }

    @Test
    void getAll_exception() {

        mongoEmployeeRepo.deleteAll();
        assertEquals(0, mongoEmployeeRepo.findAll().size());

        ServiceException exception = assertThrows(ServiceException.class, () -> service.getAll());

        assertEquals("Employee not found", exception.getMessage());

    }

    @Test
    void delete() {
        assertEquals(employee1.getEmployeeId(), mongoEmployeeRepo.findAll().get(0).getEmployeeId());
        assertEquals(employee2.getEmployeeId(), mongoEmployeeRepo.findAll().get(1).getEmployeeId());
        assertEquals(2, mongoEmployeeRepo.findAll().size());

        assertEquals("Employee with id ABC123 deleted", service.delete(employee1.getEmployeeId()));

        assertEquals(1, mongoEmployeeRepo.findAll().size());
        assertEquals(employee2.getEmployeeId(), mongoEmployeeRepo.findAll().get(0).getEmployeeId());
    }

    @Test
    void delete_exception() {
        assertEquals(employee1.getEmployeeId(), mongoEmployeeRepo.findAll().get(0).getEmployeeId());
        assertEquals(employee2.getEmployeeId(), mongoEmployeeRepo.findAll().get(1).getEmployeeId());
        assertEquals(2, mongoEmployeeRepo.findAll().size());

        ServiceException exception = assertThrows(ServiceException.class, () -> service.delete("TEST1234"));

        assertEquals("Employee not found", exception.getMessage());
        assertEquals(2, mongoEmployeeRepo.findAll().size());

        assertEquals(employee1.getEmployeeId(), mongoEmployeeRepo.findAll().get(0).getEmployeeId());
        assertEquals(employee2.getEmployeeId(), mongoEmployeeRepo.findAll().get(1).getEmployeeId());
    }

    @Test
    void getByEmployeeId() {
        assertEquals(employee1.getEmployeeId(), mongoEmployeeRepo.findAll().get(0).getEmployeeId());
        assertEquals(employee2.getEmployeeId(), mongoEmployeeRepo.findAll().get(1).getEmployeeId());
        assertEquals(2, mongoEmployeeRepo.findAll().size());

        assertEquals(employee1.getEmployeeId(), service.getByEmployeeId(employee1.getEmployeeId()).getEmployeeId());
    }

    @Test
    void getByEmployeeId_exception() {
        assertEquals(employee1.getEmployeeId(), mongoEmployeeRepo.findAll().get(0).getEmployeeId());
        assertEquals(employee2.getEmployeeId(), mongoEmployeeRepo.findAll().get(1).getEmployeeId());
        assertEquals(2, mongoEmployeeRepo.findAll().size());

        ServiceException exception = assertThrows(ServiceException.class, () -> service.getByEmployeeId("TEST1234"));

        assertEquals("Employee not found", exception.getMessage());
    }
}