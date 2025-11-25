package com.vidya.assignment.employeedetails.api;

import com.vidya.assignment.employeedetails.domain.dto.EmployeeDTO;
import com.vidya.assignment.employeedetails.service.DecisionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/employee-details")
public class EmployeeController {

    @Autowired
    private DecisionService decisionService;


    @PostMapping()
    public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody @Valid EmployeeDTO employeeDTO,
                                                      @RequestParam(required = true) String dataSource) {
        return ResponseEntity.ok(decisionService.getService(dataSource).create(employeeDTO));
    }

    @GetMapping("/get-employees")
    public ResponseEntity<List<EmployeeDTO>> findAllEmployees(@RequestParam(required = true) String dataSource) {
        return ResponseEntity.ok(decisionService.getService(dataSource).getAll());
    }

    @GetMapping()
    public ResponseEntity<EmployeeDTO> getEmployee(
            @RequestParam(required = true) String employeeId,
            @RequestParam(required = true) String dataSource) {

        EmployeeDTO employeeDTO = decisionService.getService(dataSource).getByEmployeeId(employeeId);

        return ResponseEntity.ok(employeeDTO);
    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity<String> deleteEmployee(@PathVariable String employeeId,
                                                 @RequestParam(required = true) String dataSource) {
        return ResponseEntity.ok(decisionService.getService(dataSource).delete(employeeId));
    }

    @PutMapping("/{employeeId}")
    public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable String employeeId,
                                                      @RequestBody @Valid EmployeeDTO employeeDTO,
                                                      @RequestParam(required = true) String dataSource) {
        return ResponseEntity.ok(decisionService.getService(dataSource).update(employeeId, employeeDTO));
    }
}
