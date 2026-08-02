package com.company.payroll.department.service.impl;

import com.company.payroll.department.dto.DepartmentEmployeeDTO;
import com.company.payroll.department.dto.DepartmentEmployeeDetailDTO;
import com.company.payroll.department.model.DepartmentEmployee;
import com.company.payroll.department.repository.DepartmentEmployeeRepository;
import com.company.payroll.department.service.DepartmentEmployeeService;
import com.company.payroll.exception.classes.BadRequestException;
import com.company.payroll.exception.classes.ResourceNotFoundException;
import com.company.payroll.util.util.SnowFlakeIdGenerator;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class DepartmentEmployeeServiceImpl implements DepartmentEmployeeService {
    private final SnowFlakeIdGenerator snowFlakeIdGenerator;
    private final DepartmentEmployeeRepository departmentEmployeeRepository;

    public DepartmentEmployeeServiceImpl(SnowFlakeIdGenerator snowFlakeIdGenerator,
            DepartmentEmployeeRepository departmentEmployeeRepository) {
        this.snowFlakeIdGenerator = snowFlakeIdGenerator;
        this.departmentEmployeeRepository = departmentEmployeeRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createDepartmentEmployeeInfo(DepartmentEmployeeDTO departmentEmployeeDTO) {
        Optional<Long> existingDepartmentEid = departmentEmployeeRepository
                .getDepartmentEidByDepartmentIdAndDeparmentFacilityUnitIdAndEmployeeId(
                        departmentEmployeeDTO.departmentId(),
                        departmentEmployeeDTO.departmentFUId(),
                        departmentEmployeeDTO.employeeId());

        if (existingDepartmentEid.isPresent()) {
            throw new BadRequestException(
                    "Department employee info with departmentId=" + departmentEmployeeDTO.departmentId() +
                            ", departmentFUId=" + departmentEmployeeDTO.departmentFUId() +
                            ", employeeId=" + departmentEmployeeDTO.employeeId() + " already exist.");
        }

        DepartmentEmployee departmentEmployee = new DepartmentEmployee();
        departmentEmployee.setDepartmentEid(snowFlakeIdGenerator.nextId());
        departmentEmployee.setDepartmentId(departmentEmployeeDTO.departmentId());
        departmentEmployee.setDepartmentFUId(departmentEmployeeDTO.departmentFUId());
        departmentEmployee.setEmployeeId(departmentEmployeeDTO.employeeId());
        departmentEmployee.setPrimary(departmentEmployeeDTO.isPrimary());
        departmentEmployee.setManager(departmentEmployeeDTO.isManager());
        departmentEmployee.setJoinedAt(departmentEmployeeDTO.joinedAt());
        departmentEmployee
                .setLeavedAt((departmentEmployeeDTO.leavedAt() != null) ? departmentEmployeeDTO.leavedAt() : null);

        departmentEmployeeRepository.save(departmentEmployee);
    }

    @Override
    public List<DepartmentEmployeeDetailDTO> getAllDepartmentEmployeeInfoByOffsetLimit(int offset, int limit) {
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
                        departmentEmployee.getPrimary(),
                        departmentEmployee.getManager(),
                        departmentEmployee.getJoinedAt(),
                        (departmentEmployee.getLeavedAt() != null) ? departmentEmployee.getLeavedAt() : null);

                DepartmentEmployeeDetailDTO departmentEmployeeDetailDTO = new DepartmentEmployeeDetailDTO(
                        departmentEmployee.getDepartmentEid(),
                        detail);

                departmentEmployeeeDetailDTOList.add(departmentEmployeeDetailDTO);
            }
        }

        return departmentEmployeeeDetailDTOList;
    }

    @Override
    public DepartmentEmployeeDetailDTO getDepartmentEmployeeInfoByDepartmentEid(long departmentEid) {
        DepartmentEmployee departmentEmployee = departmentEmployeeRepository.findById(departmentEid)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Department employee info with departmentEid=" + departmentEid + " not exist."));

        DepartmentEmployeeDTO detail = new DepartmentEmployeeDTO(
                departmentEmployee.getDepartmentId(),
                departmentEmployee.getDepartmentFUId(),
                departmentEmployee.getEmployeeId(),
                departmentEmployee.getPrimary(),
                departmentEmployee.getManager(),
                departmentEmployee.getJoinedAt(),
                (departmentEmployee.getLeavedAt() != null) ? departmentEmployee.getLeavedAt() : null);

        return new DepartmentEmployeeDetailDTO(
                departmentEmployee.getDepartmentEid(),
                detail);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDepartmentEmployeeInfoById(long departmentEid, DepartmentEmployeeDTO departmentEmployeeDTO) {
        DepartmentEmployee departmentEmployee = departmentEmployeeRepository.findById(departmentEid)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Department employee info with departmentEid=" + departmentEid + " not exist."));

        Optional<Long> existingDepartmentEid = departmentEmployeeRepository
                .getDepartmentEidByDepartmentIdAndDeparmentFacilityUnitIdAndEmployeeId(
                        departmentEmployeeDTO.departmentId(),
                        departmentEmployeeDTO.departmentFUId(),
                        departmentEmployeeDTO.employeeId());

        if ((existingDepartmentEid.isPresent()) && (departmentEid != existingDepartmentEid.get())) {
            throw new BadRequestException(
                    "Department employee info with departmentId=" + departmentEmployeeDTO.departmentId() +
                            ", departmentFUId=" + departmentEmployeeDTO.departmentFUId() +
                            ", employeeId=" + departmentEmployeeDTO.employeeId() + " already exist.");
        }

        departmentEmployee.setDepartmentId(departmentEmployeeDTO.departmentId());
        departmentEmployee.setDepartmentFUId(departmentEmployeeDTO.departmentFUId());
        departmentEmployee.setEmployeeId(departmentEmployeeDTO.employeeId());
        departmentEmployee.setPrimary(departmentEmployeeDTO.isPrimary());
        departmentEmployee.setManager(departmentEmployeeDTO.isManager());
        departmentEmployee.setJoinedAt(departmentEmployeeDTO.joinedAt());
        departmentEmployee
                .setLeavedAt((departmentEmployeeDTO.leavedAt() != null) ? departmentEmployeeDTO.leavedAt() : null);

        departmentEmployeeRepository.save(departmentEmployee);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDepartmentEmployeeInfoById(long departmentEid) {
        if (!departmentEmployeeRepository.existsByDepartmentEid(departmentEid)) {
            throw new BadRequestException(
                    "Department employee info with departmentEid=" + departmentEid + " not exist.");
        }

        departmentEmployeeRepository.deleteById(departmentEid);
    }
}
