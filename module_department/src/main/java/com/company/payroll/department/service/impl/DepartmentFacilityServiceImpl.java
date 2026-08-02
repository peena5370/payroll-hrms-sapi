package com.company.payroll.department.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.payroll.common.service.FacilityCommonService;
import com.company.payroll.department.dto.DepartmentDTO;
import com.company.payroll.department.dto.DepartmentFacilityDTO;
import com.company.payroll.department.dto.DepartmentFacilityDetailDTO;
import com.company.payroll.department.model.Department;
import com.company.payroll.department.model.DepartmentEmployee;
import com.company.payroll.department.model.DepartmentFacilityUnit;
import com.company.payroll.department.repository.DepartmentEmployeeRepository;
import com.company.payroll.department.repository.DepartmentFacilityUnitRepository;
import com.company.payroll.department.repository.DepartmentRepository;
import com.company.payroll.department.service.DepartmentFacilityService;
import com.company.payroll.exception.classes.BadRequestException;
import com.company.payroll.util.util.SnowFlakeIdGenerator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
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
    @Transactional(rollbackFor = Exception.class)
    public void createDepartmentFacilityUnit(DepartmentFacilityDTO departmentFacilityDTO) {
        departmentRepository.findById(departmentFacilityDTO.departmentId())
            .orElseThrow(() -> new BadRequestException("Department with departmentId=" + departmentFacilityDTO.departmentId() + " not exist."));

        if(!facilityCommonService.isCompanyFacilityExist(departmentFacilityDTO.facilityId())) {
            throw new BadRequestException("Facility with facilityId=" + departmentFacilityDTO.facilityId() + " not exist.");
        }

        if (departmentFacilityUnitRepository.existsByDepartmentIdAndFacilityId(departmentFacilityDTO.departmentId(), departmentFacilityDTO.facilityId())) {
            throw new BadRequestException("Department facility unit with departmentId=" + departmentFacilityDTO.departmentId() 
            + " and facilityId=" + departmentFacilityDTO.facilityId() + " already exist.");
        }

            DepartmentFacilityUnit departmentFacilityUnit = new DepartmentFacilityUnit();
            departmentFacilityUnit.setDepartmentFUId(snowFlakeIdGenerator.nextId());
            departmentFacilityUnit.setDepartmentId(departmentFacilityDTO.departmentId());
            departmentFacilityUnit.setFacilityId(departmentFacilityDTO.facilityId());

            departmentFacilityUnitRepository.save(departmentFacilityUnit);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDepartmentFacilityUnitDetailByDepartmentFUId(long departmentFUId) {
        departmentFacilityUnitRepository.findById(departmentFUId)
                .orElseThrow(() -> new BadRequestException("Department facility unit with departmentFUId=" + departmentFUId + " not exist."));

        departmentFacilityUnitRepository.deleteById(departmentFUId);
    }

    @Override
    public List<DepartmentFacilityDetailDTO> getAllDepartmentDetailsByFacilityId(long facilityId) {
        List<DepartmentFacilityDetailDTO> result = new ArrayList<>();

        if (facilityCommonService.isCompanyFacilityExist(facilityId)) {
            List<DepartmentFacilityUnit> departmentFacilityUnits = departmentFacilityUnitRepository
                    .getAllByFacilityId(facilityId);

            if (!departmentFacilityUnits.isEmpty()) {
                List<Long> departmentIds = departmentFacilityUnits.stream()
                        .map(DepartmentFacilityUnit::getDepartmentId)
                        .toList();

                List<Long> departmentFUIds = departmentFacilityUnits.stream()
                        .map(DepartmentFacilityUnit::getDepartmentFUId)
                        .toList();

                List<DepartmentEmployee> departmentEmployees = departmentEmployeeRepository
                        .getAllByDepartmentIdsByDepartmentFacilityUnitIdsAndIsPrimaryAndIsManager(departmentIds,
                                departmentFUIds, true, true);

                List<Department> departmentInfos = departmentRepository.getAllByDepartmentIds(departmentIds);

                if (!departmentInfos.isEmpty()) {
                    Map<Long, DepartmentEmployee> departmentEmployeeMap = departmentEmployees.stream()
                            .collect(Collectors.toMap(DepartmentEmployee::getDepartmentId,
                                    employeeInfo -> employeeInfo));

                    Map<Long, Department> departmentInfoMap = departmentInfos.stream()
                            .collect(Collectors.toMap(
                                    Department::getDepartmentId,
                                    info -> info));

                    List<DepartmentFacilityDetailDTO> finalResult = departmentFacilityUnits.stream()
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

                                return new DepartmentFacilityDetailDTO(
                                        unit.getDepartmentFUId(),
                                        unit.getDepartmentFUId(),
                                        unit.getDepartmentId(),
                                        departmentEmployeeMap.get(dept.getDepartmentId()) != null
                                                ? departmentEmployeeMap.get(dept.getDepartmentId()).getEmployeeId()
                                                : null,
                                        detail);
                            })
                            .filter(Objects::nonNull)
                            .toList();

                    result.addAll(finalResult);
                }
            }
        }

        return result;
    }
}
