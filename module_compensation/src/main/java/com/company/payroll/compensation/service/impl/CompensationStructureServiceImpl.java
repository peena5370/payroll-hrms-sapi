package com.company.payroll.compensation.service.impl;

import java.time.Instant;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.payroll.compensation.dto.CompensationStructureDTO;
import com.company.payroll.compensation.dto.CompensationStructureDetailDTO;
import com.company.payroll.compensation.mapper.CompensationStructureMapper;
import com.company.payroll.compensation.model.CompensationStructure;
import com.company.payroll.compensation.repository.CompensationStructureRepository;
import com.company.payroll.compensation.service.CompensationStructureService;
import com.company.payroll.exception.classes.DuplicateResourceException;
import com.company.payroll.exception.classes.ResourceNotFoundException;
import com.company.payroll.util.util.SnowFlakeIdGenerator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CompensationStructureServiceImpl implements CompensationStructureService {

    private final SnowFlakeIdGenerator snowFlakeIdGenerator;
    private final CompensationStructureRepository compensationStructureRepository;
    private final CompensationStructureMapper compensationStructureMapper;

    public CompensationStructureServiceImpl(SnowFlakeIdGenerator snowFlakeIdGenerator,
            CompensationStructureRepository compensationStructureRepository, CompensationStructureMapper compensationStructureMapper) {
        this.snowFlakeIdGenerator = snowFlakeIdGenerator;
        this.compensationStructureRepository = compensationStructureRepository;
        this.compensationStructureMapper = compensationStructureMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createEmployeeCompensationStructure(CompensationStructureDTO compensationStructureDTO) {
        if(compensationStructureRepository.existsByEmployeeId(compensationStructureDTO.employeeId())) {
            throw new DuplicateResourceException("Compensation structure already exists for employeeId: " + compensationStructureDTO.employeeId());
        }

        long generatedId = snowFlakeIdGenerator.nextId();

        CompensationStructure compensation = compensationStructureMapper.toEntity(compensationStructureDTO, generatedId);

        compensationStructureRepository.save(compensation);
    }

    @Override
    @Transactional(readOnly = true)
    public CompensationStructureDetailDTO getEmployeeCompensationStructureByEmployeeIdOrCompensationId(
            Long employeeId, Long compensationId) {

        return compensationStructureRepository
                .findByEmployeeId(employeeId)
                .map(compensationStructureMapper::toDetailDto)
                .orElseThrow(() ->new ResourceNotFoundException("Compensation structure not found for employeeId: " + employeeId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEmployeeCompensationStructureById(long compensationId,
            CompensationStructureDTO compensationStructureDTO) {
        CompensationStructure compensation = compensationStructureRepository.findById(compensationId)
            .orElseThrow(() -> new ResourceNotFoundException("Compensation structure not found for compensationId: " + compensationId));

        compensationStructureMapper.updateEntityFromDto(compensationStructureDTO, compensation);

        compensationStructureRepository.save(compensation);
    }

    @Override
    public void deleteEmployeeCompensationStructureById(long compensationId) {
        CompensationStructure compensation = compensationStructureRepository.findById(compensationId)
            .orElseThrow(() -> new ResourceNotFoundException("Compensation structure not found for compensationId: " + compensationId));

        compensationStructureRepository.deleteById(compensation.getCompensationId());
    }

}
