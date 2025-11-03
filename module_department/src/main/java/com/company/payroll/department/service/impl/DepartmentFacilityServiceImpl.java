package com.company.payroll.department.service.impl;

import com.company.payroll.common.FacilityCommonService;
import com.company.payroll.department.dto.DepartmentDTO;
import com.company.payroll.department.dto.DepartmentDetailDTO;
import com.company.payroll.department.dto.DepartmentFacilityDTO;
import com.company.payroll.department.model.Department;
import com.company.payroll.department.model.DepartmentEmployee;
import com.company.payroll.department.model.DepartmentFacilityUnit;
import com.company.payroll.department.repository.DepartmentEmployeeRepository;
import com.company.payroll.department.repository.DepartmentFacilityUnitRepository;
import com.company.payroll.department.repository.DepartmentRepository;
import com.company.payroll.department.service.DepartmentFacilityService;
import com.company.payroll.util.util.SnowFlakeIdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DepartmentFacilityServiceImpl implements DepartmentFacilityService {
    public static final String CLASS_NAME = "[DepartmentFacilityServiceImpl]";
    private final SnowFlakeIdGenerator snowFlakeIdGenerator;
    private final DepartmentFacilityUnitRepository departmentFacilityUnitRepository;
    private final DepartmentRepository departmentRepository;
    private final DepartmentEmployeeRepository departmentEmployeeRepository;
    private final FacilityCommonService facilityCommonService;

    public DepartmentFacilityServiceImpl(SnowFlakeIdGenerator snowFlakeIdGenerator,
                                         DepartmentFacilityUnitRepository departmentFacilityUnitRepository,
                                         DepartmentRepository departmentRepository,
                                         DepartmentEmployeeRepository departmentEmployeeRepository,
                                         FacilityCommonService facilityCommonService) {
        this.snowFlakeIdGenerator = snowFlakeIdGenerator;
        this.departmentFacilityUnitRepository = departmentFacilityUnitRepository;
        this.departmentRepository = departmentRepository;
        this.departmentEmployeeRepository = departmentEmployeeRepository;
        this.facilityCommonService = facilityCommonService;
    }

    @Override
    public int createDepartmentFacilityUnit(DepartmentFacilityDTO departmentFacilityDTO) {
        final String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("{} {} start.", CLASS_NAME, functionName);

        int status = 0;

        Optional<Department> department = departmentRepository.findById(departmentFacilityDTO.departmentId());
        boolean isFacilityExist = facilityCommonService.isCompanyFacilityExist(departmentFacilityDTO.facilityId());

        if((isFacilityExist) && (department.isPresent())) {
            DepartmentFacilityUnit departmentFacilityUnit = new DepartmentFacilityUnit(
                    snowFlakeIdGenerator.nextId(),
                    departmentFacilityDTO.departmentId(),
                    departmentFacilityDTO.facilityId());

            departmentFacilityUnitRepository.saveAndFlush(departmentFacilityUnit);

            status = 1;
        } else if(department.isEmpty()) {
            log.info("{} {} department info with departmentId={} not exist.", CLASS_NAME, functionName, departmentFacilityDTO.departmentId());
            status = -1;
        } else {
            log.info("{} {} company facility with facilityId={} not exist.", CLASS_NAME, functionName, departmentFacilityDTO.facilityId());
            status = -2;
        }

        log.info("{} {} end. Result={}", CLASS_NAME, functionName, status);
        return status;
    }

    @Override
    public boolean deleteDepartmentFacilityUnitDetailByDepartmentFUId(long departmentFUId) {
        final String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("{} {} start. departmentFUId={}", CLASS_NAME, functionName, departmentFUId);

        boolean status = false;

        Optional<DepartmentFacilityUnit> departmentFacilityUnit = departmentFacilityUnitRepository.findById(departmentFUId);

        if(departmentFacilityUnit.isEmpty()) {
            log.info("{} {} for departmentFUId={} not exist.", CLASS_NAME, functionName, departmentFUId);
        } else {
            departmentFacilityUnitRepository.delete(departmentFacilityUnit.get());
            status = true;
        }

        log.info("{} {} end. Result={}", CLASS_NAME, functionName, status);
        return status;
    }

    @Override
    public List<DepartmentDetailDTO> getAllDepartmentDetailsByFacilityId(long facilityId) {
        final String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("{} {} start. facilityId={}", CLASS_NAME, functionName, facilityId);

        List<DepartmentDetailDTO> result = new ArrayList<>();

        boolean isFacilityExist = facilityCommonService.isCompanyFacilityExist(facilityId);

        if(isFacilityExist) {
            List<DepartmentFacilityUnit> departmentFacilityUnits = departmentFacilityUnitRepository.getAllByFacilityId(facilityId);

            if(!departmentFacilityUnits.isEmpty()) {
                List<Long> departmentIds = departmentFacilityUnits.stream()
                        .map(DepartmentFacilityUnit::getDepartmentId)
                        .toList();

                List<Long> departmentFUIds = departmentFacilityUnits.stream()
                        .map(DepartmentFacilityUnit::getDepartmentFUId)
                        .toList();

                List<DepartmentEmployee> departmentEmployees = departmentEmployeeRepository
                        .getAllByDepartmentIdsAndDepartmentFacilityUnitIdsAndIsPrimaryAndIsManager(departmentIds, departmentFUIds, true, true);

                List<Department> departmentInfos = departmentRepository.getAllByDepartmentIds(departmentIds);

                if(!departmentInfos.isEmpty()) {
                    Map<Long, DepartmentEmployee> departmentEmployeeMap = departmentEmployees.stream()
                            .collect(Collectors.toMap(DepartmentEmployee::getDepartmentId,
                                    employeeInfo -> employeeInfo));

                    Map<Long, Department> departmentInfoMap = departmentInfos.stream()
                            .collect(Collectors.toMap(
                                    Department::getDepartmentId,
                                    info -> info
                            ));

                    List<DepartmentDetailDTO> finalResult = departmentFacilityUnits.stream()
                            .map(unit -> {
                                Department dept = departmentInfoMap.get(unit.getDepartmentId());

                                if (dept == null) {
                                    return null;
                                }

                                DepartmentDTO detail = new DepartmentDTO(
                                        dept.getDepartmentName(),
                                        dept.getCostCenterCode(),
                                        dept.getDescription(),
                                        dept.getParentDepartmentId(),
                                        dept.getLocation(),
                                        dept.getPhoneExtensionCode(),
                                        dept.getDepartmentEmail());

                                return new DepartmentDetailDTO(
                                        unit.getDepartmentFUId(),
                                        unit.getDepartmentId(),
                                        departmentEmployeeMap.get(dept.getDepartmentId()) != null ?
                                        departmentEmployeeMap.get(dept.getDepartmentId()).getEmployeeId() : null,
                                        detail
                                );
                            })
                            .filter(Objects::nonNull)
                            .toList();

                    result.addAll(finalResult);
                }
            }
        } else {
            log.info("{} {} company facility with facilityId={} not exist.", CLASS_NAME, functionName, facilityId);
        }

        log.info("{} {} end. facilityId={}, size={}", CLASS_NAME, functionName, facilityId, result.size());
        return result;
    }
}
