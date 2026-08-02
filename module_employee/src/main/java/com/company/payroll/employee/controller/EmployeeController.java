package com.company.payroll.employee.controller;

import com.company.payroll.employee.dto.EmployeeDTO;
import com.company.payroll.employee.service.EmployeeService;
import com.company.payroll.util.response.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestController
@RequestMapping("api/employee")
public class EmployeeController {

        private final EmployeeService employeeService;

        public EmployeeController(EmployeeService employeeService) {
                this.employeeService = employeeService;
        }

        @PostMapping
        public ResponseEntity<CommonResponse> createEmployeeDetail(@RequestBody EmployeeDTO employeeDTO) {
                employeeService.createEmployeeInfo(employeeDTO);

                return ResponseEntity.status(OK)
                                .body(new CommonResponse(OK.value(), "Employee info inserted success", null));
        }

        @GetMapping
        public ResponseEntity<CommonResponse> getAllEmployeeDetailsByOffsetAndLimit(
                        @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset,
                        @RequestParam(value = "limit", required = false, defaultValue = "5") Integer limit) {

                return ResponseEntity.status(OK)
                                .body(new CommonResponse(OK.value(), "Success retrieve employee info",
                                                employeeService.getAllEmployeesByOffsetAndLimit(offset, limit)));
        }

        @GetMapping("/{id}")
        public ResponseEntity<CommonResponse> getEmployeeDetailByEmployeeId(@PathVariable("id") Long employeeId) {

                return ResponseEntity.status(OK)
                                .body(new CommonResponse(OK.value(), "Employee info retrieve success.",
                                                employeeService.getEmployeeInfoById(employeeId)));
        }

        @PutMapping("/{id}")
        public ResponseEntity<CommonResponse> updateEmployeeDetailByEmployeeId(@PathVariable("id") Long employeeId,
                        @RequestBody EmployeeDTO employeeDTO) {
                employeeService.updateEmployeeInfoById(employeeId, employeeDTO);

                return ResponseEntity.status(OK)
                                .body(new CommonResponse(OK.value(), "Employee info updated success", null));
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<CommonResponse> deleteEmployeeDetailById(@PathVariable("id") Long employeeId) {
                employeeService.deleteEmployeeInfoById(employeeId);

                return ResponseEntity.status(OK)
                                .body(new CommonResponse(OK.value(), "Employee info delete success.", null));
        }
}
