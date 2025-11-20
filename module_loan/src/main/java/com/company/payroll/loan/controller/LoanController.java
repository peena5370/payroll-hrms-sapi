package com.company.payroll.loan.controller;

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

import com.company.payroll.util.response.CommonResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/loan")
public class LoanController {

    @PostMapping
    public ResponseEntity<CommonResponse> createEmployeeLoanApplication(@RequestBody Object loanApplicationDTO) {
        return null;
    }

    @GetMapping
    public ResponseEntity<CommonResponse> getLoanApplicationByEmployeeIdOrLoanId(
            @RequestParam(value = "employee-id", required = false) Long employeeId,
            @RequestParam(value = "loan-id", required = false) Long loanId) {
        return null;
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse> updateEmployeeLoanApplicationById(@PathVariable("id") Long loanId) {
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse> deleteEmployeeLoanApplicationById(@PathVariable("id") Long loanId) {
        return null;
    }
}
