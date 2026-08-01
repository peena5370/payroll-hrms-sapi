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
@RequestMapping("api/onetime-payment")
public class OneTimePaymentController {

    @PostMapping
    public ResponseEntity<CommonResponse> createOneTimePayment() {
        return null;
    }

    @GetMapping
    public ResponseEntity<CommonResponse> getAllOneTimePayments(
            @RequestParam(required = false, defaultValue = "0") Integer offset,
            @RequestParam(required = false, defaultValue = "5") Integer limit) {
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse> getOneTimePaymentById(@PathVariable("id") Long paymentTypeId) {
        return null;
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse> updateOneTimePaymentById(@PathVariable("id") Long paymentTypeId) {
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse> deleteOneTimePaymentById(@PathVariable("id") Long paymentTypeId) {
        return null;
    }

    @PostMapping("/employee")
    public ResponseEntity<CommonResponse> createEmployeeOneTimePayment() {
        return null;
    }

    @GetMapping("/employee")
    public ResponseEntity<CommonResponse> getAllEmployeeOneTimePaymentByPaymentEidOrEmployeeId(
            @RequestParam(value = "payment-eid", required = false) Long paymentEid,
            @RequestParam(value = "employee-id", required = false) Long employeeId) {
        return null;
    }

    @PutMapping("/employee/{id}")
    public ResponseEntity<CommonResponse> updateEmployeeOneTimePaymentById() {
        return null;
    }
}
