package com.company.payroll.department.service.impl;

import com.company.payroll.department.dto.DepartmentEmployeeDTO;
import com.company.payroll.department.dto.DepartmentEmployeeDetailDTO;
import com.company.payroll.department.model.DepartmentEmployee;
import com.company.payroll.department.repository.DepartmentEmployeeRepository;
import com.company.payroll.department.service.DepartmentEmployeeService;
import com.company.payroll.util.util.SnowFlakeIdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class DepartmentEmployeeServiceImpl implements DepartmentEmployeeService {
    public static final String CLASS_NAME = "[DepartmentServiceImpl]";

    private final SnowFlakeIdGenerator snowFlakeIdGenerator;
    private final DepartmentEmployeeRepository departmentEmployeeRepository;

    public DepartmentEmployeeServiceImpl(SnowFlakeIdGenerator snowFlakeIdGenerator,
                                         DepartmentEmployeeRepository departmentEmployeeRepository) {
        this.snowFlakeIdGenerator = snowFlakeIdGenerator;
        this.departmentEmployeeRepository = departmentEmployeeRepository;
    }

    @Override
    public int createDepartmentEmployeeInfo(DepartmentEmployeeDTO departmentEmployeeDTO) {
        final String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("{} {} start.", CLASS_NAME, functionName);

        int status = 0;
        try {
            Optional<Long> existingDepartmentEid = departmentEmployeeRepository.getDepartmentEidByDepartmentIdAndDeparmentFacilityUnitIdAndEmployeeId(
                    departmentEmployeeDTO.departmentId(),
                    departmentEmployeeDTO.departmentFUId(),
                    departmentEmployeeDTO.employeeId()
            );


            if (existingDepartmentEid.isPresent()) {
                status = -2;
            } else {
                DepartmentEmployee departmentEmployee = new DepartmentEmployee(
                        snowFlakeIdGenerator.nextId(),
                        departmentEmployeeDTO.departmentId(),
                        departmentEmployeeDTO.departmentFUId(),
                        departmentEmployeeDTO.employeeId(),
                        departmentEmployeeDTO.isPrimary(),
                        departmentEmployeeDTO.isManager(),
                        departmentEmployeeDTO.joinedAt().atStartOfDay(),
                        (departmentEmployeeDTO.leavedAt() != null) ? departmentEmployeeDTO.leavedAt().atStartOfDay() : null
                );

                departmentEmployeeRepository.saveAndFlush(departmentEmployee);

                log.info("{} {} create success.", CLASS_NAME, functionName);
                status = 1;
            }
        } catch (Exception e) {
            log.error("{} {} encountered exception. Message: {}", CLASS_NAME, functionName, e.getMessage());
            status = -1;
        }

        log.info("{} {} end. Status={}", CLASS_NAME, functionName, status);
        return status;
    }

    @Override
    public List<DepartmentEmployeeDetailDTO> getAllDepartmentEmployeeInfoByOffsetLimit(int offset, int limit) {
        final String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("{} {} start.", CLASS_NAME, functionName);

        List<DepartmentEmployeeDetailDTO> departmentEmployeeeDetailDTOList = new ArrayList<>();

        Sort sort = Sort.by("departmentEid").ascending();
        PageRequest pageRequest = PageRequest.of(offset, limit, sort);

        List<DepartmentEmployee> departmentEmployees = departmentEmployeeRepository.findAll(pageRequest).getContent();

        if (!departmentEmployees.isEmpty()) {
            for (DepartmentEmployee departmentEmployee : departmentEmployees) {
                DepartmentEmployeeDTO detail = new DepartmentEmployeeDTO(
                        departmentEmployee.getDepartmentId(),
                        departmentEmployee.getDepartmentFUId(),
                        departmentEmployee.getEmployeeId(),
                        departmentEmployee.isPrimary(),
                        departmentEmployee.isManager(),
                        departmentEmployee.getJoinedAt().toLocalDate(),
                        (departmentEmployee.getLeavedAt() != null) ? departmentEmployee.getLeavedAt().toLocalDate() : null);

                DepartmentEmployeeDetailDTO departmentEmployeeDetailDTO = new DepartmentEmployeeDetailDTO(
                        departmentEmployee.getDepartmentEid(),
                        detail
                );

                departmentEmployeeeDetailDTOList.add(departmentEmployeeDetailDTO);
            }
        }

        log.info("{} {} end. Size={}", CLASS_NAME, functionName, departmentEmployeeeDetailDTOList.size());
        return departmentEmployeeeDetailDTOList;
    }

    @Override
    public Optional<DepartmentEmployeeDetailDTO> getDepartmentEmployeeInfoByDepartmentEid(long departmentEid) {
        final String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("{} {} start. departmentEid={}", CLASS_NAME, functionName, departmentEid);

        Optional<DepartmentEmployee> departmentEmployee = departmentEmployeeRepository.findById(departmentEid);

        if (departmentEmployee.isPresent()) {
            DepartmentEmployee result = departmentEmployee.get();

            DepartmentEmployeeDTO detail = new DepartmentEmployeeDTO(
                    result.getDepartmentId(),
                    result.getDepartmentFUId(),
                    result.getEmployeeId(),
                    result.isPrimary(),
                    result.isManager(),
                    result.getJoinedAt().toLocalDate(),
                    (result.getLeavedAt() != null) ? result.getLeavedAt().toLocalDate() : null);

            DepartmentEmployeeDetailDTO departmentEmployeeDetailDTO = new DepartmentEmployeeDetailDTO(
                    result.getDepartmentEid(),
                    detail
            );

            log.info("{} {} success for departmentEid={}.", CLASS_NAME, functionName, departmentEid);
            return Optional.of(departmentEmployeeDetailDTO);
        }

        log.info("{} {} fail. Department employee info with departmentEid={} not exist.", CLASS_NAME, functionName, departmentEid);
        return Optional.empty();
    }

    @Override
    public int updateDepartmentEmployeeInfoById(long departmentEid, DepartmentEmployeeDTO departmentEmployeeDTO) {
        final String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("{} {} start. departmentEid={}", CLASS_NAME, functionName, departmentEid);

        int status = 0;

        try {
            Optional<DepartmentEmployee> departmentEmployee = departmentEmployeeRepository.findById(departmentEid);

            if (departmentEmployee.isEmpty()) {
                log.info("{} {} not found for departmentEid={}", CLASS_NAME, functionName, departmentEid);
                status = -2;
            } else {
                Optional<Long> existingDepartmentEid = departmentEmployeeRepository.getDepartmentEidByDepartmentIdAndDeparmentFacilityUnitIdAndEmployeeId(
                        departmentEmployeeDTO.departmentId(),
                        departmentEmployeeDTO.departmentFUId(),
                        departmentEmployeeDTO.employeeId()
                );

                if ((existingDepartmentEid.isPresent()) && (departmentEid != existingDepartmentEid.get())) {
                    return -3;
                } else {
                    DepartmentEmployee updateDepartmentEmployee = departmentEmployee.get();
                    updateDepartmentEmployee.setDepartmentId(departmentEmployeeDTO.departmentId());
                    updateDepartmentEmployee.setDepartmentFUId(departmentEmployeeDTO.departmentFUId());
                    updateDepartmentEmployee.setEmployeeId(departmentEmployeeDTO.employeeId());
                    updateDepartmentEmployee.setPrimary(departmentEmployeeDTO.isPrimary());
                    updateDepartmentEmployee.setManager(departmentEmployeeDTO.isManager());
                    updateDepartmentEmployee.setJoinedAt(departmentEmployeeDTO.joinedAt().atStartOfDay());
                    updateDepartmentEmployee.setLeavedAt((departmentEmployeeDTO.leavedAt() != null) ? departmentEmployeeDTO.leavedAt().atStartOfDay() : null);

                    departmentEmployeeRepository.saveAndFlush(updateDepartmentEmployee);

                    log.info("{} {} update success for departmentEid={}", CLASS_NAME, functionName, departmentEid);
                    status = 1;
                }
            }
        } catch (Exception e) {
            log.error("{} {} encountered exception. Message: {}", CLASS_NAME, functionName, e.getMessage());
            status = -1;
        }

        log.info("{} {} end. departmentEid={}, status={}", CLASS_NAME, functionName, departmentEid, status);
        return status;
    }

    @Override
    public int deleteDepartmentEmployeeInfoById(long departmentEid) {
        final String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("{} {} start. departmentEid={}", CLASS_NAME, functionName, departmentEid);

        int status = 0;

        Optional<DepartmentEmployee> departmentEmployee = departmentEmployeeRepository.findById(departmentEid);
        if (departmentEmployee.isPresent()) {
            departmentEmployeeRepository.delete(departmentEmployee.get());

            log.info("{} {} delete success for departmentEid={}", CLASS_NAME, functionName, departmentEid);
            status = 1;
        } else {
            log.info("{} {} for departmentEid={} not found.", CLASS_NAME, functionName, departmentEid);
            status = -1;
        }

        log.info("{} {} end. departmentEid={}, Status={}", CLASS_NAME, functionName, departmentEid, status);
        return status;
    }
}
