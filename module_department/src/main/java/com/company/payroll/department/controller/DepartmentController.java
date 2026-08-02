package com.company.payroll.department.controller;

import static org.springframework.http.HttpStatus.OK;

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
import com.company.payroll.department.dto.DepartmentFacilityDTO;
import com.company.payroll.department.service.DepartmentEmployeeService;
import com.company.payroll.department.service.DepartmentFacilityService;
import com.company.payroll.department.service.DepartmentService;
import com.company.payroll.util.response.CommonResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/department")
public class DepartmentController {
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
                departmentService.createDepartmentInfo(departmentDTO);

                return ResponseEntity.status(OK)
                                .body(new CommonResponse(OK.value(), "Department info inserted success", null));
        }

        @GetMapping
        public ResponseEntity<CommonResponse> getDepartmentInfoByOffsetLimit(
                        @RequestParam(required = false, defaultValue = "0") Integer offset,
                        @RequestParam(required = false, defaultValue = "5") Integer limit) {

                return ResponseEntity.status(OK)
                                .body(new CommonResponse(OK.value(), "Success retrieve department info",
                                                departmentService.getAllDepartmentInfoByOffsetLimit(offset, limit)));
        }

        @GetMapping("/{id}")
        public ResponseEntity<CommonResponse> getDepartmentInfoById(@PathVariable("id") Long departmentId) {

                return ResponseEntity.status(OK).body(new CommonResponse(OK.value(),
                                "Department info retrieve success.", departmentService
                                                .getDepartmentInfoByDepartmentId(departmentId)));
        }

        @PutMapping("/{id}")
        public ResponseEntity<Object> updateDepartmentInfoById(@PathVariable("id") Long departmentId,
                        @RequestBody DepartmentDTO departmentDTO) {
                departmentService.updateDepartmentInfoById(departmentId, departmentDTO);

                return ResponseEntity.status(OK)
                                .body(new CommonResponse(OK.value(), "Department info updated success", null));
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<CommonResponse> deleteDepartmentInfoById(@PathVariable("id") Long departmentId) {
                departmentService.deleteDepartmentInfoById(departmentId);

                return ResponseEntity.status(OK)
                                .body(new CommonResponse(OK.value(), "Department info delete success.", null));
        }

        @PostMapping("/employee")
        public ResponseEntity<CommonResponse> createDepartmentEmployee(
                        @RequestBody DepartmentEmployeeDTO departmentEmployeeDTO) {
                departmentEmployeeService.createDepartmentEmployeeInfo(departmentEmployeeDTO);

                return ResponseEntity.status(OK)
                                .body(new CommonResponse(OK.value(), "Department employee info inserted success",
                                                null));
        }

        @GetMapping("/employee")
        public ResponseEntity<CommonResponse> getAllDepartmentEmployeesByOffsetAndLimit(
                        @RequestParam(required = false, defaultValue = "0") Integer offset,
                        @RequestParam(required = false, defaultValue = "5") Integer limit) {

                return ResponseEntity.status(OK).body(new CommonResponse(OK.value(),
                                "Success retrieve department employee info",
                                departmentEmployeeService.getAllDepartmentEmployeeInfoByOffsetLimit(offset, limit)));
        }

        @GetMapping("/employee/{id}")
        public ResponseEntity<CommonResponse> getDepartmentEmployeeById(@PathVariable("id") Long departmentEid) {

                return ResponseEntity.status(OK).body(new CommonResponse(OK.value(),
                                "Department employee info retrieve success.",
                                departmentEmployeeService
                                                .getDepartmentEmployeeInfoByDepartmentEid(departmentEid)));
        }

        @PutMapping("/employee/{id}")
        public ResponseEntity<CommonResponse> updateDepartmentEmployeeById(@PathVariable("id") Long departmentEid,
                        @RequestBody DepartmentEmployeeDTO departmentEmployeeDTO) {
                departmentEmployeeService.updateDepartmentEmployeeInfoById(departmentEid, departmentEmployeeDTO);

                return ResponseEntity.status(OK)
                                .body(new CommonResponse(OK.value(), "Department employee info updated success",
                                                null));
        }

        @DeleteMapping("/employee/{id}")
        public ResponseEntity<CommonResponse> deleteDepartmentEmployeeById(@PathVariable("id") Long departmentEid) {
                departmentEmployeeService.deleteDepartmentEmployeeInfoById(departmentEid);

                return ResponseEntity.status(OK)
                                .body(new CommonResponse(OK.value(), "Department employee info delete success.", null));
        }

        @PostMapping("/unit")
        public ResponseEntity<CommonResponse> createDepartmentFacilityUnit(
                        @RequestBody DepartmentFacilityDTO departmentFacilityDTO) {
                departmentFacilityService.createDepartmentFacilityUnit(departmentFacilityDTO);

                return ResponseEntity.status(OK).body(new CommonResponse(OK.value(),
                                "Department facility unit info inserted success", null));
        }

        @GetMapping("/unit/details")
        public ResponseEntity<CommonResponse> getAllDepartmentDetailsByFacilityId(
                        @RequestParam("facility-id") Long facilityId) {

                return ResponseEntity.status(OK)
                                .body(new CommonResponse(OK.value(), "Success retrieve department details info",
                                                departmentFacilityService
                                                                .getAllDepartmentDetailsByFacilityId(facilityId)));
        }

        @DeleteMapping("/unit/{id}")
        public ResponseEntity<CommonResponse> deleteDepartmentFacilityUnitDetailByFUId(
                        @PathVariable("id") Long departmentFUId) {
                departmentFacilityService.deleteDepartmentFacilityUnitDetailByDepartmentFUId(departmentFUId);

                return ResponseEntity.status(OK).body(new CommonResponse(OK.value(),
                                "Department facility unit info delete success.", null));
        }
}
