package com.company.payroll.department.service.impl;

import com.company.payroll.department.dto.DepartmentDTO;
import com.company.payroll.department.dto.DepartmentInfoDTO;
import com.company.payroll.department.mapper.DepartmentMapper;
import com.company.payroll.department.model.Department;
import com.company.payroll.department.model.DepartmentEmployee;
import com.company.payroll.department.model.DepartmentFacilityUnit;
import com.company.payroll.department.repository.DepartmentEmployeeRepository;
import com.company.payroll.department.repository.DepartmentFacilityUnitRepository;
import com.company.payroll.department.repository.DepartmentRepository;
import com.company.payroll.department.service.DepartmentService;
import com.company.payroll.exception.classes.BadRequestException;
import com.company.payroll.exception.classes.ResourceNotFoundException;
import com.company.payroll.util.util.SnowFlakeIdGenerator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@Slf4j
@Service
@Transactional(readOnly = true)
public class DepartmentServiceImpl implements DepartmentService {
        private final SnowFlakeIdGenerator snowFlakeIdGenerator;
        private final DepartmentMapper departmentMapper;
        private final DepartmentRepository departmentRepository;
        private final DepartmentEmployeeRepository departmentEmployeeRepository;
        private final DepartmentFacilityUnitRepository departmentFacilityUnitRepository;

        public DepartmentServiceImpl(SnowFlakeIdGenerator snowFlakeIdGenerator,
                        DepartmentMapper departmentMapper,
                        DepartmentRepository departmentRepository,
                        DepartmentEmployeeRepository departmentEmployeeRepository,
                        DepartmentFacilityUnitRepository departmentFacilityUnitRepository) {
                this.snowFlakeIdGenerator = snowFlakeIdGenerator;
                this.departmentMapper = departmentMapper;
                this.departmentRepository = departmentRepository;
                this.departmentEmployeeRepository = departmentEmployeeRepository;
                this.departmentFacilityUnitRepository = departmentFacilityUnitRepository;
        }

        @Override
        public List<DepartmentInfoDTO> getAllDepartmentInfoByOffsetLimit(int offset, int limit) {
                List<DepartmentInfoDTO> departmentInfoDTOList = new ArrayList<>();

                Sort sort = Sort.by("departmentId").ascending();
                PageRequest pageRequest = PageRequest.of(offset, limit, sort);

                List<Department> departments = departmentRepository.findAll(pageRequest).getContent();

                if (!departments.isEmpty()) {
                        for (Department department : departments) {
                                DepartmentDTO detail = new DepartmentDTO(
                                                department.getDepartmentName(),
                                                department.getCostCenterCode(),
                                                department.getDescription(),
                                                department.getParentDepartmentId(),
                                                department.getLocation(),
                                                department.getPhoneExtensionCode(),
                                                department.getDepartmentEmail());

                                DepartmentInfoDTO departmentInfoDTO = new DepartmentInfoDTO(
                                                department.getDepartmentId(),
                                                department.getCreatedAt(),
                                                detail);

                                departmentInfoDTOList.add(departmentInfoDTO);
                        }
                }

                return departmentInfoDTOList;
        }

        @Override
        public DepartmentInfoDTO getDepartmentInfoByDepartmentId(long departmentId) {
                Department department = departmentRepository.findById(departmentId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Department info with departmentId=" + departmentId + " not found."));

                return departmentMapper.toDepartmentInfoDTO(department);
        }

        @Override
        @Transactional(rollbackFor = Exception.class)
        public void createDepartmentInfo(DepartmentDTO departmentDTO) {
                if (departmentRepository.existsByCostCenterCode(departmentDTO.costCenterCode())) {
                        throw new BadRequestException(
                                        "Department info with costCenterCode=" + departmentDTO.costCenterCode()
                                                        + " already exist.");
                }

                Department department = new Department();
                department.setDepartmentId(snowFlakeIdGenerator.nextId());
                department.setDepartmentName(departmentDTO.departmentName());
                department.setCostCenterCode(departmentDTO.costCenterCode());
                department.setDescription(departmentDTO.description());
                department.setParentDepartmentId(departmentDTO.parentDepartmentId());
                department.setLocation(departmentDTO.location());
                department.setPhoneExtensionCode(departmentDTO.phoneExtensionCode());
                department.setDepartmentEmail(departmentDTO.departmentEmail());
                department.setCreatedAt(Instant.now());
                department.setUpdatedAt(null);

                departmentRepository.save(department);
        }

        @Override
        @Transactional(rollbackFor = Exception.class)
        public void updateDepartmentInfoById(long departmentId, DepartmentDTO departmentDTO) {
                Department department = departmentRepository.findById(departmentId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Department info with departmentId=" + departmentId + " not found."));

                // check for duplicate department cost center value which clash with other data
                if (departmentRepository.existsByCostCenterCode(departmentDTO.costCenterCode())
                                && !department.getCostCenterCode().equals(departmentDTO.costCenterCode())) {
                        throw new BadRequestException(
                                        "Department info with costCenterCode=" + departmentDTO.costCenterCode()
                                                        + " already exist.");
                }

                department.setDepartmentName(departmentDTO.departmentName());
                department.setCostCenterCode(departmentDTO.costCenterCode());
                department.setDescription(departmentDTO.description());
                department.setParentDepartmentId(departmentDTO.parentDepartmentId());
                department.setLocation(departmentDTO.location());
                department.setPhoneExtensionCode(departmentDTO.phoneExtensionCode());
                department.setDepartmentEmail(departmentDTO.departmentEmail());
                department.setUpdatedAt(Instant.now());

                departmentRepository.save(department);
        }

        @Override
        @Transactional(rollbackFor = Exception.class)
        public void deleteDepartmentInfoById(long departmentId) {
                departmentRepository.findById(departmentId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Department info with departmentId=" + departmentId + " not found."));

                List<DepartmentEmployee> departmentEmployees = departmentEmployeeRepository
                                .getAllByDepartmentId(departmentId);
                List<DepartmentFacilityUnit> departmentFacilityUnits = departmentFacilityUnitRepository
                                .getAllByDepartmentId(departmentId);

                if (!departmentEmployees.isEmpty() || !departmentFacilityUnits.isEmpty()) {
                        throw new BadRequestException(
                                        "Department info with departmentId=" + departmentId
                                                        + " is in used, not allow to delete.");
                }

                departmentRepository.deleteById(departmentId);
        }
}
