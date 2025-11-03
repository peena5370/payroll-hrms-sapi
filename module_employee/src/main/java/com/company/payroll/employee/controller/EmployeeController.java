package com.company.payroll.employee.controller;

import com.company.payroll.employee.dto.EmployeeDTO;
import com.company.payroll.employee.dto.EmployeeInfoDTO;
import com.company.payroll.employee.service.EmployeeService;
import com.company.payroll.util.response.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@RestController
@RequestMapping("api/employee")
public class EmployeeController {

    private static final String CLASS_NAME = "[EmployeeController]";
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<CommonResponse> createEmployeeDetail(@RequestBody EmployeeDTO employeeDTO) {
        final String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("{} {} start.", CLASS_NAME, functionName);

        int result = employeeService.createEmployeeInfo(employeeDTO);

        Map<Integer, CommonResponse> responses = Map.ofEntries(
                Map.entry(1, new CommonResponse(OK.value(), "Employee info inserted success", null)),
                Map.entry(0, new CommonResponse(BAD_REQUEST.value(), "Employee info inserted failed", null)),
                Map.entry(-1, new CommonResponse(INTERNAL_SERVER_ERROR.value(), "API exception encountered. Please check backend log for status", null)),
                Map.entry(-2, new CommonResponse(BAD_REQUEST.value(), "Employee info exist", null))
        );

        CommonResponse response = responses.getOrDefault(result, new CommonResponse(INTERNAL_SERVER_ERROR.value(),
                CommonResponse.COMMON_ERROR_MESSAGE, null));

        log.info("{} {} end. Response={}", CLASS_NAME, functionName, response.statusCode());
        return ResponseEntity.status(response.statusCode()).body(response);
    }

    @GetMapping
    public ResponseEntity<CommonResponse> getAllEmployeeDetailsByOffsetAndLimit(
            @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset,
            @RequestParam(value = "limit", required = false, defaultValue = "5") Integer limit) {
        final String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("{} {} start. Request param: offset={}, limit={}", CLASS_NAME, functionName, offset, limit);

        List<EmployeeInfoDTO> employeeList = employeeService.getAllEmployeesByOffsetAndLimit(offset, limit);

        CommonResponse response = employeeList.isEmpty() ?
                new CommonResponse(BAD_REQUEST.value(), "Error when retrieving the employee info.", null) :
                new CommonResponse(OK.value(), "Success retrieve employee info", employeeList);

        log.info("{} {} end. Response={}", CLASS_NAME, functionName, response.statusCode());
        return ResponseEntity.status(response.statusCode()).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse> getEmployeeDetailByEmployeeId(@PathVariable("id") Long employeeId) {
        final String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("{} {} start. departmentId={}.", CLASS_NAME, functionName, employeeId);

        Optional<EmployeeInfoDTO> employeeInfo = employeeService.getEmployeeInfoById(employeeId);

        CommonResponse response = employeeInfo.map(employeeInfoDTO ->
                        new CommonResponse(OK.value(), "Employee info retrieve success.", employeeInfoDTO))
                .orElseGet(() -> new CommonResponse(NOT_FOUND.value(), "Employee info not found.", null));

        log.info("{} {} end. Status={}", CLASS_NAME, functionName, response.statusCode());
        return ResponseEntity.status(response.statusCode()).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse> updateEmployeeDetailByEmployeeId(@PathVariable("id") Long employeeId, @RequestBody EmployeeDTO employeeDTO) {
        final String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("{} {} start. employeeId={}", CLASS_NAME, functionName, employeeId);
        int updatedResult = employeeService.updateEmployeeInfoById(employeeId, employeeDTO);

        Map<Integer, CommonResponse> responses = Map.ofEntries(
                Map.entry(1, new CommonResponse(OK.value(), "Employee info updated success", null)),
                Map.entry(0, new CommonResponse(BAD_REQUEST.value(), "Employee info updated failed", null)),
                Map.entry(-1, new CommonResponse(INTERNAL_SERVER_ERROR.value(), "API exception encountered. Please check backend log for status", null)),
                Map.entry(-2, new CommonResponse(NOT_FOUND.value(), "Employee info not found", null)),
                Map.entry(-3, new CommonResponse(BAD_REQUEST.value(), "Employee ic number duplicated", null))
        );

        CommonResponse response = responses.getOrDefault(updatedResult, new CommonResponse(INTERNAL_SERVER_ERROR.value(),
                CommonResponse.COMMON_ERROR_MESSAGE, null));

        log.info("{} {} end. employeeId={}, Status={}", CLASS_NAME, functionName, employeeId, response.statusCode());
        return ResponseEntity.status(response.statusCode()).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse> deleteEmployeeDetailById(@PathVariable("id") Long employeeId) {
        final String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("{} {} start. employeeId={}", CLASS_NAME, functionName, employeeId);

        int deletedResult = employeeService.deleteEmployeeInfoById(employeeId);

        Map<Integer, CommonResponse> responses = Map.ofEntries(
                Map.entry(1, new CommonResponse(OK.value(), "Employee info delete success.", null)),
                Map.entry(0, new CommonResponse(BAD_REQUEST.value(), "Employee info delete failed.", null)),
                Map.entry(-1, new CommonResponse(NOT_FOUND.value(), "Employee info not found", null)),
                Map.entry(-2, new CommonResponse(BAD_REQUEST.value(), "Employee info is in used, not allow to delete", null))
        );

        CommonResponse response = responses.getOrDefault(deletedResult, new CommonResponse(INTERNAL_SERVER_ERROR.value(),
                CommonResponse.COMMON_ERROR_MESSAGE, null));

        log.info("{} {} end. employeeId={}, Status={}", CLASS_NAME, functionName, employeeId, response.statusCode());
        return ResponseEntity.status(response.statusCode()).body(response);
    }
}
