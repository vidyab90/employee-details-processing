package com.vidya.assignment.employee.details.processing.domain.mongo.service;

import com.vidya.assignment.employee.details.processing.domain.dto.EmployeeDTO;
import com.vidya.assignment.employee.details.processing.domain.mongo.entity.EmployeeMongo;
import com.vidya.assignment.employee.details.processing.domain.mongo.repo.MongoEmployeeRepo;
import com.vidya.assignment.employee.details.processing.domain.util.DomainService;
import com.vidya.assignment.employee.details.processing.domain.util.EntityMapper;
import com.vidya.assignment.employee.details.processing.exception.ServiceException;
import com.vidya.assignment.employee.details.processing.model.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class JsonEmployeeService implements DomainService<EmployeeDTO> {

    @Autowired
    private MongoEmployeeRepo mongoEmployeeRepo;


    @Override
    public EmployeeDTO create(EmployeeDTO employeeDTO) {

        if (mongoEmployeeRepo.existsByEmployeeId(employeeDTO.getEmployeeId())) {
            log.warn("Employee with EmployeeID {} already exists", employeeDTO.getEmployeeId());
            throw new ServiceException(ErrorCode.EMPLOYEE_ALREADY_EXISTS, "Employee already exists");
        }

        EmployeeMongo employee = EntityMapper.mapDTOtoJsonEmployee(employeeDTO);
        EmployeeMongo savedEmployee = mongoEmployeeRepo.save(employee);

        return EntityMapper.mapJsonToEmployeeDTO(savedEmployee);
    }

    @Override
    public EmployeeDTO update(String employeeId, EmployeeDTO employeeDTO) {
        Optional<EmployeeMongo> employee = mongoEmployeeRepo.findFirstByEmployeeId(employeeId);
        if (employee.isEmpty()) {
            log.warn("Employee with id {} does not exist", employeeId);
            throw new ServiceException(ErrorCode.EMPLOYEE_NOT_FOUND, "Employee not found");
        }
        employee.ifPresent(
                it -> {
                    it.setEmployeeId(employeeDTO.getEmployeeId());
                    it.setEmail(employeeDTO.getEmail());
                    it.setFirstName(employeeDTO.getFirstName());
                    it.setLastName(employeeDTO.getLastName());
                    it.setDateOfBirth(employeeDTO.getDateOfBirth());
                    it.setPhoneNo(employeeDTO.getPhoneNo());
                });
        log.info("Employee with id {} updated", employeeId);
        return EntityMapper.mapJsonToEmployeeDTO(mongoEmployeeRepo.save(employee.get()));
    }

    @Override
    public List<EmployeeDTO> getAll() {
        List<EmployeeDTO> employeesList = mongoEmployeeRepo.findAll()
                .stream()
                .map(EntityMapper::mapJsonToEmployeeDTO)
                .toList();

        if (employeesList.isEmpty()) {
            throw new ServiceException(ErrorCode.EMPLOYEE_NOT_FOUND, "Employee not found");
        }
        return employeesList;
    }

    @Override
    public String delete(String employeeId) {
        if (!mongoEmployeeRepo.existsByEmployeeId(employeeId)) {
            log.error("Employee with id {} not found", employeeId);
            throw new ServiceException(ErrorCode.EMPLOYEE_NOT_FOUND, "Employee not found");
        } else {
            mongoEmployeeRepo.deleteByEmployeeId(employeeId);
            log.info("Employee with id {} deleted", employeeId);
            return "Employee with id " + employeeId + " deleted";
        }
    }

    @Override
    public EmployeeDTO getByEmployeeId(String employeeId) {
        Optional<EmployeeDTO> employee = mongoEmployeeRepo.findFirstByEmployeeId(employeeId)
                .map(EntityMapper::mapJsonToEmployeeDTO);
        if (employee.isPresent()) {
            return employee.get();
        } else {
            throw new ServiceException(ErrorCode.EMPLOYEE_NOT_FOUND, "Employee not found");
        }

    }
}
