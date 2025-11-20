package com.company.payroll.compensation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.company.payroll.compensation.service.CompensationStructureService;
import com.company.payroll.util.response.CommonResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/compensation")
public class CompensationController {

    private CompensationStructureService compensationStructureService;

    public CompensationController(CompensationStructureService compensationStructureService) {
        this.compensationStructureService = compensationStructureService;
    }

    @PostMapping
    public ResponseEntity<CommonResponse> createEmployeeCompensationStructure(
            @RequestBody Object compensationStructureDTO) {
        return null;
    }

    @GetMapping
    public ResponseEntity<CommonResponse> getEmployeeCompensationStructureByEmployeeIdOrCompensationId(
            @RequestParam(value = "employee-id", required = false) Long employeeId,
            @RequestParam(value = "compensation-id", required = false) Long compensationId) {
        return null;
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse> updateEmployeeCompensationStructureById(
            @PathVariable("id") Long compensationId) {
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse> deleteEmployeeCompensationStructureById(
            @PathVariable("id") Long compensationId) {
        return null;
    }
}
