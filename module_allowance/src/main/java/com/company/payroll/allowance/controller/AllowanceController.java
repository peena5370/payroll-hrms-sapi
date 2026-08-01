package com.company.payroll.allowance.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.company.payroll.util.response.CommonResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/allowance")
public class AllowanceController {

    @PostMapping
    public ResponseEntity<CommonResponse> createAllowance() {
        return null;
    }

    @GetMapping
    public ResponseEntity<CommonResponse> getAllAllowances(
            @RequestParam(required = false, defaultValue = "0") Integer offset,
            @RequestParam(required = false, defaultValue = "5") Integer limit) {
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse> getAllowanceById(@PathVariable("id") Long allowanceId) {
        return null;
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse> updateAllowance() {
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse> deleteAllowance(@PathVariable("id") Long allowanceId) {
        return null;
    }

    @PostMapping("/employee")
    public ResponseEntity<CommonResponse> createAllowanceForEmployee() {
        return null;
    }

    @GetMapping("/employee")
    public ResponseEntity<CommonResponse> getAllEmployeeAllowancesByAllowanceEidOrEmployeeId(
            @RequestParam(value = "allowance-eid", required = false) Long allowanceEid,
            @RequestParam(value = "employee-id", required = false) Long employeeId) {
        return null;
    }

    @PutMapping("/employee/{id}")
    public ResponseEntity<CommonResponse> updateAllowanceEmployeeByAllowanceEmployeeId(
            @PathVariable("id") Long allowanceEid) {
        return null;
    }

    @DeleteMapping("/employee/{id}")
    public ResponseEntity<CommonResponse> deleteAllowanceEmployeeByAllowanceEmployeeId(
            @PathVariable("id") Long allowanceEid) {
        return null;
    }
}
