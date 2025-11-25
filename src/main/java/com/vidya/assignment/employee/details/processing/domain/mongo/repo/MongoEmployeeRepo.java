package com.vidya.assignment.employee.details.processing.domain.mongo.repo;

import com.vidya.assignment.employee.details.processing.domain.mongo.entity.EmployeeMongo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface MongoEmployeeRepo extends MongoRepository<EmployeeMongo, String> {
    Optional<EmployeeMongo> findFirstByEmployeeId(String employeeId);

    Boolean existsByEmployeeId(String employeeId);

    void deleteByEmployeeId(String employeeId);

}
