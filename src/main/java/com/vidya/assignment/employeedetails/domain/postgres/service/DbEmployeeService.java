package com.vidya.assignment.employeedetails.domain.postgres.service;

import com.vidya.assignment.employeedetails.domain.dto.EmployeeDTO;
import com.vidya.assignment.employeedetails.domain.postgres.entity.Employee;
import com.vidya.assignment.employeedetails.domain.postgres.repo.EmployeeRepo;
import com.vidya.assignment.employeedetails.domain.util.DomainService;
import com.vidya.assignment.employeedetails.domain.util.EntityMapper;
import com.vidya.assignment.employeedetails.exception.ServiceException;
import com.vidya.assignment.employeedetails.model.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class DbEmployeeService implements DomainService<EmployeeDTO> {

    @Autowired
    private EmployeeRepo employeeRepo;

    @Transactional
    @Override
    public EmployeeDTO create(EmployeeDTO employeeDTO) {

        if ((employeeDTO.getId() != null && employeeRepo.existsById(Long.parseLong(employeeDTO.getId())))) {
            log.warn("Employee with id {} already exists", employeeDTO.getId());
            throw new ServiceException(ErrorCode.EMPLOYEE_ALREADY_EXISTS, "Employee already exists");
        }

        if (employeeRepo.existsByEmployeeId(employeeDTO.getEmployeeId())) {
            log.warn("Employee with EmployeeID {} already exists", employeeDTO.getEmployeeId());
            throw new ServiceException(ErrorCode.EMPLOYEE_ALREADY_EXISTS, "Employee already exists");
        }

        EmployeeDTO employeeDTOCreated = EntityMapper.mapEmployeeToDTO(
                employeeRepo.save(EntityMapper.mapEmployeeFromDTO(employeeDTO)));

        log.info("Employee with id {} created", employeeDTOCreated.getId());

        return employeeDTOCreated;
    }

    @Transactional
    @Override
    public EmployeeDTO update(String employeeId, EmployeeDTO employeeDTO) {

        Optional<Employee> employee = employeeRepo.findFirstByEmployeeId(employeeId);

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
                }
        );

        log.info("Employee with id {} updated", employeeId);
        return EntityMapper.mapEmployeeToDTO(employeeRepo.save(employee.get()));
    }

    @Transactional(readOnly = true)
    @Override
    public List<EmployeeDTO> getAll() {

        List<EmployeeDTO> list = employeeRepo.findAll()
                .stream()
                .map(EntityMapper::mapEmployeeToDTO)
                .toList();

        if (list.isEmpty()) {
            log.warn("Employee not found");
            throw new ServiceException(ErrorCode.EMPLOYEE_NOT_FOUND, "Employee not found");
        }
        return list;
    }

    @Transactional
    @Override
    public String delete(String employeeId) {
        if (!employeeRepo.existsByEmployeeId(employeeId)) {
            log.error("Employee with id {} not found", employeeId);
            throw new ServiceException(ErrorCode.EMPLOYEE_NOT_FOUND, "Employee not found");
        } else {
            employeeRepo.deleteByEmployeeId(employeeId);
            log.info("Employee with id {} deleted", employeeId);
            return "Employee with id " + employeeId + " deleted";
        }
    }

    @Transactional(readOnly = true)
    public EmployeeDTO getByEmployeeId(String employeeId) {

        Optional<EmployeeDTO> employee = employeeRepo.findFirstByEmployeeId(employeeId)
                .map(EntityMapper::mapEmployeeToDTO);

        if (employee.isPresent()) {
            log.debug("Employee with employeeID {} exists", employeeId);
            return employee.get();
        } else {
            log.warn("Employee with employeeID {} not found", employeeId);
            throw new ServiceException(ErrorCode.EMPLOYEE_NOT_FOUND, "Employee not found");
        }
    }
}
