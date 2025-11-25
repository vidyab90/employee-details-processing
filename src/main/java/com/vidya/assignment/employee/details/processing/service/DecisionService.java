package com.vidya.assignment.employee.details.processing.service;

import com.vidya.assignment.employee.details.processing.domain.dto.EmployeeDTO;
import com.vidya.assignment.employee.details.processing.domain.mongo.service.JsonEmployeeService;
import com.vidya.assignment.employee.details.processing.domain.postgres.service.DbEmployeeService;
import com.vidya.assignment.employee.details.processing.domain.util.DomainService;
import com.vidya.assignment.employee.details.processing.exception.ServiceException;
import com.vidya.assignment.employee.details.processing.model.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DecisionService {

    @Autowired
    private DbEmployeeService dbEmployeeService;

    @Autowired
    private JsonEmployeeService jsonEmployeeService;

    public DomainService<EmployeeDTO> getService(String dataSource) {

        log.info("Matching Employee Service");

        if ("db".equalsIgnoreCase(dataSource)) {
            return dbEmployeeService;
        } else if ("json".equalsIgnoreCase(dataSource)) {
            return jsonEmployeeService;
        }

        throw new ServiceException(ErrorCode.INVALID_DATA_SOURCE, "Invalid data source: " + dataSource);
    }
}
