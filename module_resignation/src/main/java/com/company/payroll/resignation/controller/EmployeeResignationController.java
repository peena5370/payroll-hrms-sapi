package com.company.payroll.resignation.controller;

import com.company.payroll.resignation.dto.EmployeeResignationDetailDTO;
import com.company.payroll.resignation.service.EmployeeResignationService;
import com.company.payroll.resignation.dto.EmployeeResignationDTO;
import com.company.payroll.util.response.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@RestController
@RequestMapping("api/resignation")
public class EmployeeResignationController {

    private static final String CLASS_NAME = "[EmployeePromotionController]";
    private final EmployeeResignationService employeeResignationService;

    public EmployeeResignationController(EmployeeResignationService employeeResignationService) {
        this.employeeResignationService = employeeResignationService;
    }

    @PostMapping
    public ResponseEntity<CommonResponse> createResignationDetail(@RequestBody EmployeeResignationDTO employeeResignationDTO) {
        final String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("{} {} start.", CLASS_NAME, functionName);

        int result = employeeResignationService.createResignationInfo(employeeResignationDTO);

        Map<Integer, CommonResponse> responses = Map.ofEntries(
                Map.entry(1, new CommonResponse(OK.value(), "Employee resignation info inserted success", null)),
                Map.entry(0, new CommonResponse(BAD_REQUEST.value(), "Employee resignation info inserted failed", null)),
                Map.entry(-1, new CommonResponse(INTERNAL_SERVER_ERROR.value(), "API exception encountered. Please check backend log for status", null))
        );

        CommonResponse response = responses.getOrDefault(result, new CommonResponse(INTERNAL_SERVER_ERROR.value(),
                CommonResponse.COMMON_ERROR_MESSAGE, null));

        log.info("{} {} end. Response={}", CLASS_NAME, functionName, response.statusCode());
        return ResponseEntity.status(response.statusCode()).body(response);
    }

    @GetMapping
    public ResponseEntity<CommonResponse> getAllResignationDetailsByOffsetAndLimitOrEmployeeId(
            @RequestParam(value = "employeeId", required = false) Long employeeId,
            @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset,
            @RequestParam(value = "limit", required = false, defaultValue = "5") Integer limit) {
        final String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("{} {} start. Request param: employeeId={} offset={}, limit={}", CLASS_NAME, functionName, employeeId, offset, limit);

        List<EmployeeResignationDetailDTO> resignationList = employeeResignationService.getAllResignationInfoByOffsetAndLimitOrEmployeeId(employeeId, offset, limit);

        CommonResponse response = resignationList.isEmpty() ?
                new CommonResponse(BAD_REQUEST.value(), "Error when retrieving the employee resignation info.", null) :
                new CommonResponse(OK.value(), "Success retrieve employee resignation info", resignationList);

        log.info("{} {} end. Response={}", CLASS_NAME, functionName, response.statusCode());
        return ResponseEntity.status(response.statusCode()).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse> getResignationDetailByResignationId(@PathVariable("id") Long resignationId) {
        final String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("{} {} start. resignationId={}.", CLASS_NAME, functionName, resignationId);

        Optional<EmployeeResignationDetailDTO> resignationInfo = employeeResignationService.getResignationInfoById(resignationId);

        CommonResponse response = resignationInfo.map(resignationInfoDTO ->
                        new CommonResponse(OK.value(), "Employee resignation info retrieve success.", resignationInfoDTO))
                .orElseGet(() -> new CommonResponse(NOT_FOUND.value(), "Employee resignation info not found.", null));

        log.info("{} {} end. Status={}", CLASS_NAME, functionName, response.statusCode());
        return ResponseEntity.status(response.statusCode()).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse> updateResignationDetailByResignationId(@PathVariable("id") Long resignationId,
                                                                                 @RequestBody EmployeeResignationDTO employeeResignationDTO) {
        final String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("{} {} start. resignationId={}", CLASS_NAME, functionName, resignationId);

        int updatedResult = employeeResignationService.updateResignationInfoById(resignationId, employeeResignationDTO);

        Map<Integer, CommonResponse> responses = Map.ofEntries(
                Map.entry(1, new CommonResponse(OK.value(), "Employee resignation info updated success", null)),
                Map.entry(0, new CommonResponse(BAD_REQUEST.value(), "Employee resignation info updated failed", null)),
                Map.entry(-1, new CommonResponse(INTERNAL_SERVER_ERROR.value(), "API exception encountered. Please check backend log for status", null)),
                Map.entry(-2, new CommonResponse(NOT_FOUND.value(), "Employee resignation info not found", null))
        );

        CommonResponse response = responses.getOrDefault(updatedResult, new CommonResponse(INTERNAL_SERVER_ERROR.value(),
                CommonResponse.COMMON_ERROR_MESSAGE, null));

        log.info("{} {} end. resignationId={}, Status={}", CLASS_NAME, functionName, resignationId, response.statusCode());
        return ResponseEntity.status(response.statusCode()).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse> deleteResignationDetailByResignationId(@PathVariable("id") Long resignationId) {
        final String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("{} {} start. resignationId={}", CLASS_NAME, functionName, resignationId);

        int deletedResult = employeeResignationService.deleteResignationInfoById(resignationId);

        Map<Integer, CommonResponse> responses = Map.ofEntries(
                Map.entry(1, new CommonResponse(OK.value(), "Employee resignation info delete success.", null)),
                Map.entry(0, new CommonResponse(BAD_REQUEST.value(), "Employee resignation info delete failed.", null)),
                Map.entry(-1, new CommonResponse(NOT_FOUND.value(), "Employee resignation info not found", null))
        );

        CommonResponse response = responses.getOrDefault(deletedResult, new CommonResponse(INTERNAL_SERVER_ERROR.value(),
                CommonResponse.COMMON_ERROR_MESSAGE, null));

        log.info("{} {} end. resignationId={}, Status={}", CLASS_NAME, functionName, resignationId, response.statusCode());
        return ResponseEntity.status(response.statusCode()).body(response);
    }
}
