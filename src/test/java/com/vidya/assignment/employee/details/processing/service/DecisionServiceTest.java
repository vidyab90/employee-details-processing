package com.vidya.assignment.employee.details.processing.service;

import com.vidya.assignment.employee.details.processing.domain.mongo.service.JsonEmployeeService;
import com.vidya.assignment.employee.details.processing.domain.postgres.service.DbEmployeeService;
import com.vidya.assignment.employee.details.processing.exception.ServiceException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(classes = DecisionService.class)
class DecisionServiceTest {

    @MockitoBean
    private JsonEmployeeService jsonEmployeeService;

    @MockitoBean
    private DbEmployeeService dbEmployeeService;

    @Autowired
    private DecisionService decisionService;

    @Test
    void getService_db() {
        Assertions.assertEquals(dbEmployeeService, decisionService.getService("db"));
    }

    @Test
    void getService_json() {
        Assertions.assertEquals(jsonEmployeeService, decisionService.getService("json"));
    }

    @Test
    void getService_exception() {
        Assertions.assertThrows(ServiceException.class, () -> decisionService.getService("test"));
    }
}