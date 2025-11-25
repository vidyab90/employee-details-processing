package com.vidya.assignment.employeedetails.api;

import com.vidya.assignment.employeedetails.domain.dto.EmployeeDTO;
import com.vidya.assignment.employeedetails.domain.util.DomainService;
import com.vidya.assignment.employeedetails.exception.ServiceException;
import com.vidya.assignment.employeedetails.model.ErrorCode;
import com.vidya.assignment.employeedetails.service.DecisionService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @MockitoBean
    private DomainService<EmployeeDTO> domainService;

    @MockitoBean
    private DecisionService decisionService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private EmployeeDTO employeeDTO;
    private EmployeeDTO employeeDTO2;
    private List<EmployeeDTO> employeeDTOList;


    @BeforeEach
    void setUp() {

        employeeDTO = EmployeeDTO.builder()
                .id("1")
                .employeeId("ABC01")
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(LocalDate.parse("1980-01-01"))
                .email("jone@gmail.com")
                .phoneNo("123456789")
                .build();

        employeeDTO2 = EmployeeDTO.builder()
                .id("2")
                .employeeId("ABC02")
                .firstName("Johnergs")
                .lastName("Doexcn")
                .dateOfBirth(LocalDate.parse("1973-04-23"))
                .email("jonexdfdf@gmail.com")
                .phoneNo("14587546789")
                .build();

        employeeDTOList = List.of(employeeDTO, employeeDTO2);

        when(decisionService.getService(any())).thenReturn(domainService);

    }

    @AfterEach
    void tearDown() {
        Mockito.reset(domainService);
    }

    @Test
    void createEmployee() throws Exception {
        when(domainService.create(any())).thenReturn(employeeDTO);

        String json = objectMapper.writeValueAsString(employeeDTO);

        log.info(employeeDTO.toString());
        log.info(json);

        mockMvc.perform(
                        post("/employee-details")
                                .param("dataSource", "db")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)

                )
                .andExpect(status().isOk())
                .andExpect(content().json(json));

        verify(domainService, times(1)).create(any());
    }

    @Test
    void createEmployee_exception() throws Exception {
        when(domainService.create(any())).thenReturn(employeeDTO);

        String json = "testData";

        mockMvc.perform(
                        post("/employee-details")
                                .param("dataSource", "db")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().is5xxServerError())
                .andExpect(content().json("{\"code\":\"ERROR_001\",\"message\":\"Internal Server Error\"}"));

        verify(domainService, times(0)).create(any());
    }

    @Test
    void findAllEmployees() throws Exception {
        when(domainService.getAll()).thenReturn(employeeDTOList);
        String json = objectMapper.writeValueAsString(employeeDTOList);

        mockMvc.perform(
                        get("/employee-details/get-employees")
                                .param("dataSource", "db")
                ).andExpect(status().isOk())
                .andExpect(content().json(json));

        verify(domainService, times(1)).getAll();

    }


    @Test
    void findAllEmployees_Exception() throws Exception {
        when(domainService.getAll()).thenThrow(new ServiceException(ErrorCode.EMPLOYEE_NOT_FOUND, "Employee not found"));

        mockMvc.perform(
                        get("/employee-details/get-employees")
                                .param("dataSource", "db")
                ).andExpect(status().is4xxClientError())
                .andExpect(content().json("{\"code\":\"EMP003\",\"message\":\"Employee not found\"}"));

        verify(domainService, times(1)).getAll();

    }

    @Test
    void getEmployee_byEmployeeId() throws Exception {
        when(domainService.getByEmployeeId(any())).thenReturn(employeeDTO);
        String json = objectMapper.writeValueAsString(employeeDTO);

        mockMvc.perform(get("/employee-details")
                        .param("employeeId", employeeDTO.getEmployeeId().toString())
                        .param("dataSource", "db")
                )
                .andExpect(status().isOk())
                .andExpect(content().json(json));

        verify(domainService, times(1)).getByEmployeeId(any());
    }

    @Test
    void getEmployee_Exception() throws Exception {

        mockMvc.perform(
                        get("/employee-details")
                                .param("dataSource", "db")
                ).andExpect(status().is5xxServerError())
                .andExpect(content().json("{\"code\":\"ERROR_001\",\"message\":\"Internal Server Error\"}"));

        verify(domainService, times(0)).getByEmployeeId(any());
    }

    @Test
    void deleteEmployee() throws Exception {
        when(domainService.delete(any())).thenReturn("Employee with id \" + id + \" deleted");

        mockMvc.perform(delete("/employee-details/" + employeeDTO.getEmployeeId())
                        .param("dataSource", "db")
                )
                .andExpect(status().isOk())
                .andExpect(content().string("Employee with id \" + id + \" deleted"));

        verify(domainService, times(1)).delete(employeeDTO.getEmployeeId());
        verify(domainService, times(1)).delete(any());
    }

    @Test
    void updateEmployee() throws Exception {
        when(domainService.update(any(), any())).thenReturn(employeeDTO);

        String json = objectMapper.writeValueAsString(employeeDTO);

        log.info(employeeDTO.toString());
        log.info(json);

        mockMvc.perform(
                        put("/employee-details/" + employeeDTO.getEmployeeId())
                                .param("dataSource", "db")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(json));

        verify(domainService, times(1)).update(any(), any());
    }
}