package com.company.payroll.promotion.controller;

import com.company.payroll.promotion.dto.EmployeePromotionDTO;
import com.company.payroll.promotion.dto.EmployeePromotionDetailDTO;
import com.company.payroll.promotion.service.EmployeePromotionService;
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
@RequestMapping("api/promotion")
public class EmployeePromotionController {

    private static final String CLASS_NAME = "[EmployeePromotionController]";
    private final EmployeePromotionService employeePromotionService;

    public EmployeePromotionController(EmployeePromotionService employeePromotionService) {
        this.employeePromotionService = employeePromotionService;
    }

    @PostMapping
    public ResponseEntity<CommonResponse> createPromotionDetail(@RequestBody EmployeePromotionDTO employeePromotionDTO) {
        final String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("{} {} start.", CLASS_NAME, functionName);

        int result = employeePromotionService.createPromotionDetail(employeePromotionDTO);

        Map<Integer, CommonResponse> responses = Map.ofEntries(
                Map.entry(1, new CommonResponse(OK.value(), "Employee promotion info inserted success", null)),
                Map.entry(0, new CommonResponse(BAD_REQUEST.value(), "Employee promotion info inserted failed", null)),
                Map.entry(-1, new CommonResponse(INTERNAL_SERVER_ERROR.value(), "API exception encountered. Please check backend log for status", null))
        );

        CommonResponse response = responses.getOrDefault(result, new CommonResponse(INTERNAL_SERVER_ERROR.value(),
                CommonResponse.COMMON_ERROR_MESSAGE, null));

        log.info("{} {} end. Response={}", CLASS_NAME, functionName, response.statusCode());
        return ResponseEntity.status(response.statusCode()).body(response);
    }

    @GetMapping
    public ResponseEntity<CommonResponse> getAllPromotionDetailByOffsetAndLimit(
            @RequestParam(value = "employeeId", required = false) Long employeeId,
            @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset,
            @RequestParam(value = "limit", required = false, defaultValue = "5") Integer limit) {
        final String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("{} {} start. Request param: employeeId={} offset={}, limit={}", CLASS_NAME, functionName, employeeId, offset, limit);

        List<EmployeePromotionDetailDTO> promotionList = employeePromotionService.getAllPromotionsByOffsetAndLimitOrEmployeeId(employeeId, offset, limit);

        CommonResponse response = promotionList.isEmpty() ?
                new CommonResponse(BAD_REQUEST.value(), "Error when retrieving the employee promotion info.", null) :
                new CommonResponse(OK.value(), "Success retrieve employee promotion info", promotionList);

        log.info("{} {} end. Response={}", CLASS_NAME, functionName, response.statusCode());
        return ResponseEntity.status(response.statusCode()).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse> getPromotionDetailByPromotionId(@PathVariable("id") Long promotionId) {
        final String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("{} {} start. promotionId={}.", CLASS_NAME, functionName, promotionId);

        Optional<EmployeePromotionDetailDTO> promotionInfo = employeePromotionService.getPromotionDetailById(promotionId);

        CommonResponse response = promotionInfo.map(promotionInfoDTO ->
                        new CommonResponse(OK.value(), "Employee promotion info retrieve success.", promotionInfoDTO))
                .orElseGet(() -> new CommonResponse(NOT_FOUND.value(), "Employee promotion info not found.", null));

        log.info("{} {} end. Status={}", CLASS_NAME, functionName, response.statusCode());
        return ResponseEntity.status(response.statusCode()).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse> updatePromotionDetailByPromotionId(@PathVariable("id") Long promotionId,
                                                                             @RequestBody EmployeePromotionDTO employeePromotionDTO) {
        final String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("{} {} start. promotionId={}", CLASS_NAME, functionName, promotionId);

        int updatedResult = employeePromotionService.updatePromotionDetailById(promotionId, employeePromotionDTO);

        Map<Integer, CommonResponse> responses = Map.ofEntries(
                Map.entry(1, new CommonResponse(OK.value(), "Employee promotion info updated success", null)),
                Map.entry(0, new CommonResponse(BAD_REQUEST.value(), "Employee promotion info updated failed", null)),
                Map.entry(-1, new CommonResponse(INTERNAL_SERVER_ERROR.value(), "API exception encountered. Please check backend log for status", null)),
                Map.entry(-2, new CommonResponse(NOT_FOUND.value(), "Employee promotion info not found", null))
        );

        CommonResponse response = responses.getOrDefault(updatedResult, new CommonResponse(INTERNAL_SERVER_ERROR.value(),
                CommonResponse.COMMON_ERROR_MESSAGE, null));

        log.info("{} {} end. promotionId={}, Status={}", CLASS_NAME, functionName, promotionId, response.statusCode());
        return ResponseEntity.status(response.statusCode()).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse> deletePromotionDetailByPromotionId(@PathVariable("id") Long promotionId) {
        final String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("{} {} start. promotionId={}", CLASS_NAME, functionName, promotionId);

        int deletedResult = employeePromotionService.deletePromotionDetailById(promotionId);

        Map<Integer, CommonResponse> responses = Map.ofEntries(
                Map.entry(1, new CommonResponse(OK.value(), "Employee promotion info delete success.", null)),
                Map.entry(0, new CommonResponse(BAD_REQUEST.value(), "Employee promotion info delete failed.", null)),
                Map.entry(-1, new CommonResponse(NOT_FOUND.value(), "Employee promotion info not found", null))
        );

        CommonResponse response = responses.getOrDefault(deletedResult, new CommonResponse(INTERNAL_SERVER_ERROR.value(),
                CommonResponse.COMMON_ERROR_MESSAGE, null));

        log.info("{} {} end. promotionId={}, Status={}", CLASS_NAME, functionName, promotionId, response.statusCode());
        return ResponseEntity.status(response.statusCode()).body(response);
    }
}
