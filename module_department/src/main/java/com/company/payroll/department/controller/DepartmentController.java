package com.company.payroll.department.controller;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

import java.util.List;
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

import com.company.payroll.department.dto.DepartmentDTO;
import com.company.payroll.department.dto.DepartmentEmployeeDTO;
import com.company.payroll.department.dto.DepartmentEmployeeDetailDTO;
import com.company.payroll.department.dto.DepartmentFacilityDTO;
import com.company.payroll.department.dto.DepartmentFacilityDetailDTO;
import com.company.payroll.department.dto.DepartmentInfoDTO;
import com.company.payroll.department.service.DepartmentEmployeeService;
import com.company.payroll.department.service.DepartmentFacilityService;
import com.company.payroll.department.service.DepartmentService;
import com.company.payroll.util.response.CommonResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/department")
public class DepartmentController {
    private static final String CLASS_NAME = "[DepartmentController]";

    private final DepartmentService departmentService;
    private final DepartmentEmployeeService departmentEmployeeService;
    private final DepartmentFacilityService departmentFacilityService;

    public DepartmentController(DepartmentService departmentService,
                                DepartmentEmployeeService departmentEmployeeService,
                                DepartmentFacilityService departmentFacilityService) {
        this.departmentService = departmentService;
        this.departmentEmployeeService = departmentEmployeeService;
        this.departmentFacilityService = departmentFacilityService;
    }

    @PostMapping
    public ResponseEntity<CommonResponse> createDepartment(@RequestBody DepartmentDTO departmentDTO) {
        final String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("{} {} start.", CLASS_NAME, functionName);

        int result = departmentService.createDepartmentInfo(departmentDTO);

        Map<Integer, CommonResponse> responses = Map.ofEntries(
                Map.entry(1, new CommonResponse(OK.value(), "Department info inserted success", null)),
                Map.entry(0, new CommonResponse(BAD_REQUEST.value(), "Department info inserted failed", null)),
                Map.entry(-1, new CommonResponse(INTERNAL_SERVER_ERROR.value(), "API exception encountered. Please check backend log for status", null)),
                Map.entry(-2, new CommonResponse(BAD_REQUEST.value(), "Department info exist", null))
        );

        CommonResponse response = responses.getOrDefault(result, new CommonResponse(INTERNAL_SERVER_ERROR.value(),
                CommonResponse.COMMON_ERROR_MESSAGE, null));

        log.info("{} {} end. Response={}", CLASS_NAME, functionName, response.statusCode());
        return ResponseEntity.status(response.statusCode()).body(response);
    }

    @GetMapping
    public ResponseEntity<CommonResponse> getDepartmentInfoByOffsetLimit
            (@RequestParam(required = false, defaultValue = "0") Integer offset,
             @RequestParam(required = false, defaultValue = "5") Integer limit) {
        final String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("{} {} start. Request param: offset={}, limit={}", CLASS_NAME, functionName, offset, limit);

        List<DepartmentInfoDTO> departmentList = departmentService.getAllDepartmentInfoByOffsetLimit(offset, limit);

        CommonResponse response = departmentList.isEmpty() ?
                new CommonResponse(BAD_REQUEST.value(), "Error when retrieving the department info.", null) :
                new CommonResponse(OK.value(), "Success retrieve department info", departmentList);

        log.info("{} {} end. Response={}", CLASS_NAME, functionName, response.statusCode());
        return ResponseEntity.status(response.statusCode()).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse> getDepartmentInfoById(@PathVariable("id") Long departmentId) {
        final String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("{} {} start. departmentId={}.", CLASS_NAME, functionName, departmentId);

        Optional<DepartmentInfoDTO> departmentInfo = departmentService.getDepartmentInfoByDepartmentId(departmentId);

        CommonResponse response = departmentInfo.map(departmentInfoDTO ->
                        new CommonResponse(OK.value(), "Department info retrieve success.", departmentInfoDTO))
                .orElseGet(() -> new CommonResponse(NOT_FOUND.value(), "Department info not found.", null));

        log.info("{} {} end. Status={}", CLASS_NAME, functionName, response.statusCode());
        return ResponseEntity.status(response.statusCode()).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateDepartmentInfoById(@PathVariable("id") Long departmentId, @RequestBody DepartmentDTO departmentDTO) {
        final String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("{} {} start. departmentId={}", CLASS_NAME, functionName, departmentId);
        int updatedResult = departmentService.updateDepartmentInfoById(departmentId, departmentDTO);

        Map<Integer, CommonResponse> responses = Map.ofEntries(
                Map.entry(1, new CommonResponse(OK.value(), "Department info updated success", null)),
                Map.entry(0, new CommonResponse(BAD_REQUEST.value(), "Department info updated failed", null)),
                Map.entry(-1, new CommonResponse(INTERNAL_SERVER_ERROR.value(), "API exception encountered. Please check backend log for status", null)),
                Map.entry(-2, new CommonResponse(NOT_FOUND.value(), "Department info not found", null)),
                Map.entry(-3, new CommonResponse(BAD_REQUEST.value(), "Department cost center code duplicated", null))
        );

        CommonResponse response = responses.getOrDefault(updatedResult, new CommonResponse(INTERNAL_SERVER_ERROR.value(),
                CommonResponse.COMMON_ERROR_MESSAGE, null));

        log.info("{} {} end. departmentId={}, Status={}", CLASS_NAME, functionName, departmentId, response.statusCode());
        return ResponseEntity.status(response.statusCode()).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse> deleteDepartmentInfoById(@PathVariable("id") Long departmentId) {
        final String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("{} {} start. departmentId={}", CLASS_NAME, functionName, departmentId);

        int deletedResult = departmentService.deleteDepartmentInfoById(departmentId);

        Map<Integer, CommonResponse> responses = Map.ofEntries(
                Map.entry(1, new CommonResponse(OK.value(), "Department info delete success.", null)),
                Map.entry(0, new CommonResponse(BAD_REQUEST.value(), "Department info delete failed.", null)),
                Map.entry(-1, new CommonResponse(NOT_FOUND.value(), "Department info not found", null)),
                Map.entry(-2, new CommonResponse(BAD_REQUEST.value(), "Department info is in used, not allow to delete", null))
        );

        CommonResponse response = responses.getOrDefault(deletedResult, new CommonResponse(INTERNAL_SERVER_ERROR.value(),
                CommonResponse.COMMON_ERROR_MESSAGE, null));

        log.info("{} {} end. departmentId={}, Status={}", CLASS_NAME, functionName, departmentId, response.statusCode());
        return ResponseEntity.status(response.statusCode()).body(response);
    }

    @PostMapping("/employee")
    public ResponseEntity<CommonResponse> createDepartmentEmployee(@RequestBody DepartmentEmployeeDTO departmentEmployeeDTO) {
        final String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("{} {} start.", CLASS_NAME, functionName);

        int result = departmentEmployeeService.createDepartmentEmployeeInfo(departmentEmployeeDTO);

        Map<Integer, CommonResponse> responses = Map.ofEntries(
                Map.entry(1, new CommonResponse(OK.value(), "Department employee info inserted success", null)),
                Map.entry(0, new CommonResponse(BAD_REQUEST.value(), "Department employee info inserted failed", null)),
                Map.entry(-1, new CommonResponse(INTERNAL_SERVER_ERROR.value(), "API exception encountered. Please check backend log for status", null)),
                Map.entry(-2, new CommonResponse(BAD_REQUEST.value(), "Department employee info exist", null))
        );

        CommonResponse response = responses.getOrDefault(result, new CommonResponse(INTERNAL_SERVER_ERROR.value(),
                CommonResponse.COMMON_ERROR_MESSAGE, null));

        log.info("{} {} end. Response={}", CLASS_NAME, functionName, response.statusCode());
        return ResponseEntity.status(response.statusCode()).body(response);
    }

    @GetMapping("/employee")
    public ResponseEntity<CommonResponse> getAllDepartmentEmployeesByOffsetAndLimit(
            @RequestParam(required = false, defaultValue = "0") Integer offset,
            @RequestParam(required = false, defaultValue = "5") Integer limit) {
        final String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("{} {} start. Request param: offset={}, limit={}", CLASS_NAME, functionName, offset, limit);

        List<DepartmentEmployeeDetailDTO> departmentEmployeeList = departmentEmployeeService.getAllDepartmentEmployeeInfoByOffsetLimit(offset, limit);

        CommonResponse response = departmentEmployeeList.isEmpty() ?
                new CommonResponse(BAD_REQUEST.value(), "Error when retrieving the department employee info.", null) :
                new CommonResponse(OK.value(), "Success retrieve department employee info", departmentEmployeeList);

        log.info("{} {} end. Response={}", CLASS_NAME, functionName, response.statusCode());
        return ResponseEntity.status(response.statusCode()).body(response);
    }

    @GetMapping("/employee/{id}")
    public ResponseEntity<CommonResponse> getDepartmentEmployeeById(@PathVariable("id") Long departmentEid) {
        final String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("{} {} start. departmentEid={}.", CLASS_NAME, functionName, departmentEid);

        Optional<DepartmentEmployeeDetailDTO> departmentEmployeeDetailDTO = departmentEmployeeService.getDepartmentEmployeeInfoByDepartmentEid(departmentEid);

        CommonResponse response = departmentEmployeeDetailDTO.map(departmentInfoDTO ->
                        new CommonResponse(OK.value(), "Department employee info retrieve success.", departmentEmployeeDetailDTO))
                .orElseGet(() -> new CommonResponse(NOT_FOUND.value(), "Department employee info not found.", null));

        log.info("{} {} end. Status={}", CLASS_NAME, functionName, response.statusCode());
        return ResponseEntity.status(response.statusCode()).body(response);
    }

    @PutMapping("/employee/{id}")
    public ResponseEntity<CommonResponse> updateDepartmentEmployeeById(@PathVariable("id") Long departmentEid, @RequestBody DepartmentEmployeeDTO departmentEmployeeDTO) {
        final String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("{} {} start. departmentEid={}", CLASS_NAME, functionName, departmentEid);
        int updatedResult = departmentEmployeeService.updateDepartmentEmployeeInfoById(departmentEid, departmentEmployeeDTO);

        Map<Integer, CommonResponse> responses = Map.ofEntries(
                Map.entry(1, new CommonResponse(OK.value(), "Department employee info updated success", null)),
                Map.entry(0, new CommonResponse(BAD_REQUEST.value(), "Department employee info updated failed", null)),
                Map.entry(-1, new CommonResponse(INTERNAL_SERVER_ERROR.value(), "API exception encountered. Please check backend log for status", null)),
                Map.entry(-2, new CommonResponse(NOT_FOUND.value(), "Department employee info not found", null)),
                Map.entry(-3, new CommonResponse(BAD_REQUEST.value(), "Department info duplicated", null))
        );

        CommonResponse response = responses.getOrDefault(updatedResult, new CommonResponse(INTERNAL_SERVER_ERROR.value(),
                CommonResponse.COMMON_ERROR_MESSAGE, null));

        log.info("{} {} end. departmentEid={}, Status={}", CLASS_NAME, functionName, departmentEid, response.statusCode());
        return ResponseEntity.status(response.statusCode()).body(response);
    }

    @DeleteMapping("/employee/{id}")
    public ResponseEntity<CommonResponse> deleteDepartmentEmployeeById(@PathVariable("id") Long departmentEid) {
        final String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("{} {} start. departmentEid={}", CLASS_NAME, functionName, departmentEid);

        int deletedResult = departmentEmployeeService.deleteDepartmentEmployeeInfoById(departmentEid);

        Map<Integer, CommonResponse> responses = Map.ofEntries(
                Map.entry(1, new CommonResponse(OK.value(), "Department employee info delete success.", null)),
                Map.entry(0, new CommonResponse(BAD_REQUEST.value(), "Department employee info delete failed.", null)),
                Map.entry(-1, new CommonResponse(NOT_FOUND.value(), "Department employee info not found", null))
        );

        CommonResponse response = responses.getOrDefault(deletedResult, new CommonResponse(INTERNAL_SERVER_ERROR.value(),
                CommonResponse.COMMON_ERROR_MESSAGE, null));

        log.info("{} {} end. departmentEid={}, Status={}", CLASS_NAME, functionName, departmentEid, response.statusCode());
        return ResponseEntity.status(response.statusCode()).body(response);
    }

    @PostMapping("/unit")
    public ResponseEntity<CommonResponse> createDepartmentFacilityUnit(@RequestBody DepartmentFacilityDTO departmentFacilityDTO) {
        final String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("{} {} start.", CLASS_NAME, functionName);

        int result = departmentFacilityService.createDepartmentFacilityUnit(departmentFacilityDTO);

        Map<Integer, CommonResponse> responses = Map.ofEntries(
                Map.entry(1, new CommonResponse(OK.value(), "Department facility unit info inserted success", null)),
                Map.entry(0, new CommonResponse(BAD_REQUEST.value(), "Department facility unit info inserted failed", null)),
                Map.entry(-1, new CommonResponse(BAD_REQUEST.value(), "Department/Facility info not exist", null)),
                Map.entry(-2, new CommonResponse(BAD_REQUEST.value(), "Company facility unit info exist", null))
        );

        CommonResponse response = responses.getOrDefault(result, new CommonResponse(INTERNAL_SERVER_ERROR.value(),
                CommonResponse.COMMON_ERROR_MESSAGE, null));

        log.info("{} {} end. Response={}", CLASS_NAME, functionName, response.statusCode());
        return ResponseEntity.status(response.statusCode()).body(response);
    }

    @GetMapping("/unit/details")
    public ResponseEntity<CommonResponse> getAllDepartmentDetailsByFacilityId(@RequestParam("facility-id") Long facilityId) {
        final String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("{} {} start.", CLASS_NAME, functionName);

        List<DepartmentFacilityDetailDTO> departmentFacilityDetailList = departmentFacilityService.getAllDepartmentDetailsByFacilityId(facilityId);

        CommonResponse response = departmentFacilityDetailList.isEmpty() ?
                new CommonResponse(BAD_REQUEST.value(), "Error when retrieving the department details info.", null) :
                new CommonResponse(OK.value(), "Success retrieve department details info", departmentFacilityDetailList);

        log.info("{} {} end. Response={}", CLASS_NAME, functionName, response.statusCode());
        return ResponseEntity.status(response.statusCode()).body(response);
    }

    @DeleteMapping("/unit/{id}")
    public ResponseEntity<CommonResponse> deleteDepartmentFacilityUnitDetailByFUId(@PathVariable("id") Long departmentFUId) {
        final String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("{} {} start. departmentFUId={}", CLASS_NAME, functionName, departmentFUId);

        boolean deletedResult = departmentFacilityService.deleteDepartmentFacilityUnitDetailByDepartmentFUId(departmentFUId);

        Map<Boolean, CommonResponse> responses = Map.ofEntries(
                Map.entry(true, new CommonResponse(OK.value(), "Department facility unit info delete success.", null)),
                Map.entry(false, new CommonResponse(BAD_REQUEST.value(), "Department facility unit info delete failed.", null))
        );

        CommonResponse response = responses.getOrDefault(deletedResult, new CommonResponse(INTERNAL_SERVER_ERROR.value(),
                CommonResponse.COMMON_ERROR_MESSAGE, null));

        log.info("{} {} end. departmentFUId={}, Status={}", CLASS_NAME, functionName, departmentFUId, response.statusCode());
        return ResponseEntity.status(response.statusCode()).body(response);
    }
}
