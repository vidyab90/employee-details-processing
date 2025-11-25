package com.vidya.assignment.employeedetails.domain.postgres.repo;

import com.vidya.assignment.employeedetails.domain.postgres.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, Long> {

    Boolean existsByEmployeeId(String employeeId);

    Optional<Employee> findFirstByEmployeeId(String employeeID);

    void deleteByEmployeeId(String employeeId);


}
