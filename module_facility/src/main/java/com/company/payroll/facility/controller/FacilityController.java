package com.company.payroll.facility.controller;

import com.company.payroll.facility.dto.CompanyFacilityDTO;
import com.company.payroll.facility.dto.CompanyFacilityDetailDTO;
import com.company.payroll.facility.service.CompanyFacilityService;
import com.company.payroll.util.response.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestController
@RequestMapping("api/facility")
public class FacilityController {

    private static final String CLASS_NAME = "[FacilityController]";
    private final CompanyFacilityService companyFacilityService;

    public FacilityController(CompanyFacilityService companyFacilityService) {
        this.companyFacilityService = companyFacilityService;
    }

    @PostMapping
    public ResponseEntity<CommonResponse> createCompanyFacility(@RequestBody CompanyFacilityDTO companyFacilityDTO) {
        final String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("{} {} start.", CLASS_NAME, functionName);

        int result = companyFacilityService.createCompanyFacilityDetail(companyFacilityDTO);

        Map<Integer, CommonResponse> responses = Map.ofEntries(
                Map.entry(1, new CommonResponse(OK.value(), "Company facility info inserted success", null)),
                Map.entry(0, new CommonResponse(BAD_REQUEST.value(), "Company facility info inserted failed", null)),
                Map.entry(-1, new CommonResponse(INTERNAL_SERVER_ERROR.value(), "API exception encountered. Please check backend log for status", null)),
                Map.entry(-2, new CommonResponse(BAD_REQUEST.value(), "Company facility info exist", null))
        );

        CommonResponse response = responses.getOrDefault(result, new CommonResponse(INTERNAL_SERVER_ERROR.value(),
                CommonResponse.COMMON_ERROR_MESSAGE, null));

        log.info("{} {} end. Response={}", CLASS_NAME, functionName, response.statusCode());
        return ResponseEntity.status(response.statusCode()).body(response);
    }

    @GetMapping
    public ResponseEntity<CommonResponse> getCompanyFacilityDetailsByOffsetAndLimit(
            @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset,
            @RequestParam(value = "limit", required = false, defaultValue = "5") Integer limit) {
        final String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("{} {} start. Request param: offset={}, limit={}", CLASS_NAME, functionName, offset, limit);

        List<CompanyFacilityDetailDTO> facilityList = companyFacilityService.getAllCompanyFacilityDetailByOffsetAndLimit(offset, limit);

        CommonResponse response = facilityList.isEmpty() ?
                new CommonResponse(BAD_REQUEST.value(), "Error when retrieving the company facility info.", null) :
                new CommonResponse(OK.value(), "Success retrieve company facility info", facilityList);

        log.info("{} {} end. Response={}", CLASS_NAME, functionName, response.statusCode());
        return ResponseEntity.status(response.statusCode()).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse> getCompanyFacilityDetailById(@PathVariable("id") Long facilityId) {
        final String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("{} {} start. facilityId={}.", CLASS_NAME, functionName, facilityId);

        Optional<CompanyFacilityDetailDTO> facilityInfo = companyFacilityService.getCompanyFacilityDetailById(facilityId);

        CommonResponse response = facilityInfo.map(facilityInfoDTO ->
                        new CommonResponse(OK.value(), "Company facility info retrieve success.", facilityInfoDTO))
                .orElseGet(() -> new CommonResponse(NOT_FOUND.value(), "Company facility info not found.", null));

        log.info("{} {} end. Status={}", CLASS_NAME, functionName, response.statusCode());
        return ResponseEntity.status(response.statusCode()).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse> updateCompanyFacilityDetailById(@PathVariable("id") Long facilityId, @RequestBody CompanyFacilityDTO companyFacilityDTO) {
        final String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("{} {} start. facilityId={}", CLASS_NAME, functionName, facilityId);
        int updatedResult = companyFacilityService.updateCompanyFacilityDetailById(facilityId, companyFacilityDTO);

        Map<Integer, CommonResponse> responses = Map.ofEntries(
                Map.entry(1, new CommonResponse(OK.value(), "Company facility info updated success", null)),
                Map.entry(0, new CommonResponse(BAD_REQUEST.value(), "Company facility info updated failed", null)),
                Map.entry(-1, new CommonResponse(INTERNAL_SERVER_ERROR.value(), "API exception encountered. Please check backend log for status", null)),
                Map.entry(-2, new CommonResponse(NOT_FOUND.value(), "Company facility info not found", null)),
                Map.entry(-3, new CommonResponse(BAD_REQUEST.value(), "Company facility name duplicated", null))
        );

        CommonResponse response = responses.getOrDefault(updatedResult, new CommonResponse(INTERNAL_SERVER_ERROR.value(),
                CommonResponse.COMMON_ERROR_MESSAGE, null));

        log.info("{} {} end. facilityId={}, Status={}", CLASS_NAME, functionName, facilityId, response.statusCode());
        return ResponseEntity.status(response.statusCode()).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse> deleteCompanyFacilityDetailById(@PathVariable("id") Long facilityId) {
        final String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("{} {} start. facilityId={}", CLASS_NAME, functionName, facilityId);

        int deletedResult = companyFacilityService.deleteCompanyFacilityDetailById(facilityId);

        Map<Integer, CommonResponse> responses = Map.ofEntries(
                Map.entry(1, new CommonResponse(OK.value(), "Company facility info delete success.", null)),
                Map.entry(0, new CommonResponse(BAD_REQUEST.value(), "Company facility info delete failed.", null)),
                Map.entry(-1, new CommonResponse(NOT_FOUND.value(), "Company facility info not found", null)),
                Map.entry(-2, new CommonResponse(BAD_REQUEST.value(), "Company facility info is in used, not allow to delete", null))
        );

        CommonResponse response = responses.getOrDefault(deletedResult, new CommonResponse(INTERNAL_SERVER_ERROR.value(),
                CommonResponse.COMMON_ERROR_MESSAGE, null));

        log.info("{} {} end. facilityId={}, Status={}", CLASS_NAME, functionName, facilityId, response.statusCode());
        return ResponseEntity.status(response.statusCode()).body(response);
    }
}
