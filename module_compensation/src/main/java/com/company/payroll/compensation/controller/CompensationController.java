package com.company.payroll.compensation.controller;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

import java.util.Map;
import java.util.Optional;

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

import com.company.payroll.compensation.dto.CompensationStructureDTO;
import com.company.payroll.compensation.dto.CompensationStructureDetailDTO;
import com.company.payroll.compensation.service.CompensationStructureService;
import com.company.payroll.util.response.CommonResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/compensation")
public class CompensationController {

    private static final String CLASS_NAME = "[CompensationController]";

    private CompensationStructureService compensationStructureService;

    public CompensationController(CompensationStructureService compensationStructureService) {
        this.compensationStructureService = compensationStructureService;
    }

    @PostMapping
    public ResponseEntity<CommonResponse> createEmployeeCompensationStructure(
            @RequestBody CompensationStructureDTO compensationStructureDTO) {
        final String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("{} {} start.", CLASS_NAME, functionName);

        int result = compensationStructureService.createEmployeeCompensationStructure(compensationStructureDTO);

        Map<Integer, CommonResponse> responses = Map.ofEntries(
                Map.entry(1, new CommonResponse(OK.value(), "Compensation info inserted success", null)),
                Map.entry(0, new CommonResponse(BAD_REQUEST.value(), "Compensation info inserted failed", null)),
                Map.entry(-1,
                        new CommonResponse(INTERNAL_SERVER_ERROR.value(),
                                "API exception encountered. Please check backend log for status", null)),
                Map.entry(-2, new CommonResponse(BAD_REQUEST.value(), "Compensation info exist", null)));

        CommonResponse response = responses.getOrDefault(result, new CommonResponse(INTERNAL_SERVER_ERROR.value(),
                CommonResponse.COMMON_ERROR_MESSAGE, null));

        log.info("{} {} end. Response={}", CLASS_NAME, functionName, response.statusCode());
        return ResponseEntity.status(response.statusCode()).body(response);
    }

    @GetMapping
    public ResponseEntity<CommonResponse> getEmployeeCompensationStructureByEmployeeIdOrCompensationId(
            @RequestParam(value = "employee-id", required = false) Long employeeId,
            @RequestParam(value = "compensation-id", required = false) Long compensationId) {
        final String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("{} {} start. employeeId={}, compensationId={}.", CLASS_NAME, functionName, employeeId,
                compensationId);

        Optional<CompensationStructureDetailDTO> compensationDetailInfo = compensationStructureService
                .getEmployeeCompensationStructureByEmployeeIdOrCompensationId(employeeId, compensationId);

        CommonResponse response = compensationDetailInfo
                .map(departmentInfoDTO -> new CommonResponse(OK.value(), "Compensation info retrieve success.",
                        departmentInfoDTO))
                .orElseGet(() -> new CommonResponse(NOT_FOUND.value(), "Compensation info not found.", null));

        log.info("{} {} end. Status={}", CLASS_NAME, functionName, response.statusCode());
        return ResponseEntity.status(response.statusCode()).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse> updateEmployeeCompensationStructureById(
            @PathVariable("id") Long compensationId, @RequestBody CompensationStructureDTO compensationStructureDTO) {
        final String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("{} {} start. compensationId={}", CLASS_NAME, functionName, compensationId);
        int updatedResult = compensationStructureService.updateEmployeeCompensationStructureById(compensationId, compensationStructureDTO);

        Map<Integer, CommonResponse> responses = Map.ofEntries(
                Map.entry(1, new CommonResponse(OK.value(), "Compensation info updated success", null)),
                Map.entry(0, new CommonResponse(BAD_REQUEST.value(), "Compensation info updated failed", null)),
                Map.entry(-1,
                        new CommonResponse(INTERNAL_SERVER_ERROR.value(),
                                "API exception encountered. Please check backend log for status", null)),
                Map.entry(-2, new CommonResponse(NOT_FOUND.value(), "Compensation info not found", null)),
                Map.entry(-3, new CommonResponse(BAD_REQUEST.value(), "Compensation cost center code duplicated", null)));

        CommonResponse response = responses.getOrDefault(updatedResult,
                new CommonResponse(INTERNAL_SERVER_ERROR.value(),
                        CommonResponse.COMMON_ERROR_MESSAGE, null));

        log.info("{} {} end. compensationId={}, Status={}", CLASS_NAME, functionName, compensationId,
                response.statusCode());
        return ResponseEntity.status(response.statusCode()).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse> deleteEmployeeCompensationStructureById(
            @PathVariable("id") Long compensationId) {
                final String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("{} {} start. compensationId={}", CLASS_NAME, functionName, compensationId);

        int deletedResult = compensationStructureService.deleteEmployeeCompensationStructureById(compensationId);

        Map<Integer, CommonResponse> responses = Map.ofEntries(
                Map.entry(1, new CommonResponse(OK.value(), "Compensation info delete success.", null)),
                Map.entry(0, new CommonResponse(BAD_REQUEST.value(), "Compensation info delete failed.", null)),
                Map.entry(-1, new CommonResponse(NOT_FOUND.value(), "Compensation info not found", null)),
                Map.entry(-2, new CommonResponse(BAD_REQUEST.value(), "Compensation info is in used, not allow to delete", null))
        );

        CommonResponse response = responses.getOrDefault(deletedResult, new CommonResponse(INTERNAL_SERVER_ERROR.value(),
                CommonResponse.COMMON_ERROR_MESSAGE, null));

        log.info("{} {} end. compensationId={}, Status={}", CLASS_NAME, functionName, compensationId, response.statusCode());
        return ResponseEntity.status(response.statusCode()).body(response);
    }
}
