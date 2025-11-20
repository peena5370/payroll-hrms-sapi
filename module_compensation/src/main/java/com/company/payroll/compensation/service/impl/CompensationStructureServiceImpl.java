package com.company.payroll.compensation.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.company.payroll.compensation.dto.CompensationStructureDTO;
import com.company.payroll.compensation.dto.CompensationStructureDetailDTO;
import com.company.payroll.compensation.model.CompensationStructure;
import com.company.payroll.compensation.repository.CompensationStructureRepository;
import com.company.payroll.compensation.service.CompensationStructureService;
import com.company.payroll.util.util.SnowFlakeIdGenerator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CompensationStructureServiceImpl implements CompensationStructureService {

    private static final String CLASS_NAME = "[CompensationStructureServiceImpl]";

    private final SnowFlakeIdGenerator snowFlakeIdGenerator;
    private CompensationStructureRepository compensationStructureRepository;

    public CompensationStructureServiceImpl(SnowFlakeIdGenerator snowFlakeIdGenerator,
            CompensationStructureRepository compensationStructureRepository) {
        this.snowFlakeIdGenerator = snowFlakeIdGenerator;
        this.compensationStructureRepository = compensationStructureRepository;
    }

    @Override
    public int createEmployeeCompensationStructure(CompensationStructureDTO compensationStructureDTO) {
        final String functionName = Thread.currentThread().getStackTrace()[2].getMethodName();
        log.info("{} {} start.", CLASS_NAME, functionName);

        int status = 0;
        try {
            Optional<CompensationStructure> existingCompensationStructure = compensationStructureRepository
                    .findByEmployeeId(compensationStructureDTO.employeeId());

            if (existingCompensationStructure.isPresent()) {
                status = -2;
            } else {
                CompensationStructure compensation = new CompensationStructure(
                        snowFlakeIdGenerator.nextId(),
                        compensationStructureDTO.employeeId(),
                        compensationStructureDTO.baseAnnualSalary(),
                        compensationStructureDTO.paymentFrequency(),
                        compensationStructureDTO.effectiveDate(),
                        compensationStructureDTO.isActive(),
                        compensationStructureDTO.epfEmployeeRate(),
                        compensationStructureDTO.epfEmployerRate(),
                        compensationStructureDTO.socsoGroup(),
                        LocalDateTime.now(),
                        null);

                compensationStructureRepository.saveAndFlush(compensation);

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
    public Optional<CompensationStructureDetailDTO> getEmployeeCompensationStructureByEmployeeIdOrCompensationId(
            Long employeeId, Long compensationId) {
        final String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("{} {} start. employeeId={}, compensationId={}", CLASS_NAME, functionName, employeeId, compensationId);

        Optional<CompensationStructureDetailDTO> compensationStructureDetailDTO = Optional.empty();

        if (employeeId != null) {
            Optional<CompensationStructure> employeeCompensation = compensationStructureRepository
                    .findByEmployeeId(employeeId);

            if (employeeCompensation.isPresent()) {
                CompensationStructure result = employeeCompensation.get();

                CompensationStructureDTO detail = new CompensationStructureDTO(
                        result.getEmployeeId(),
                        result.getBaseAnnualSalary(),
                        result.getPaymentFrequency(),
                        result.getEffectiveDate(),
                        result.isActive(),
                        result.getEpfEmployeeRate(),
                        result.getEpfEmployerRate(),
                        result.getSocsoGroup());

                CompensationStructureDetailDTO resultDTO = new CompensationStructureDetailDTO(
                        result.getCompensationId(),
                        result.getCreatedAt(),
                        detail);

                compensationStructureDetailDTO = Optional.of(resultDTO);
            }
        } else {
            Optional<CompensationStructure> compensation = compensationStructureRepository.findById(compensationId);

            if (compensation.isPresent()) {
                CompensationStructure result = compensation.get();

                CompensationStructureDTO detail = new CompensationStructureDTO(
                        result.getEmployeeId(),
                        result.getBaseAnnualSalary(),
                        result.getPaymentFrequency(),
                        result.getEffectiveDate(),
                        result.isActive(),
                        result.getEpfEmployeeRate(),
                        result.getEpfEmployerRate(),
                        result.getSocsoGroup());

                CompensationStructureDetailDTO resultDTO = new CompensationStructureDetailDTO(
                        result.getCompensationId(),
                        result.getCreatedAt(),
                        detail);

                compensationStructureDetailDTO = Optional.of(resultDTO);
            }
        }

        log.info("{} {} end. Result is={}", CLASS_NAME, functionName, compensationStructureDetailDTO.isPresent());
        return compensationStructureDetailDTO;
    }

    @Override
    public int updateEmployeeCompensationStructureById(long compensationId,
            CompensationStructureDTO compensationStructureDTO) {
        final String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("{} {} start. compensationId={}", CLASS_NAME, functionName, compensationId);

        int status = 0;

        try {
            Optional<CompensationStructure> compensation = compensationStructureRepository.findById(compensationId);

            if (compensation.isEmpty()) {
                log.info("{} {} not found for compensationId={}", CLASS_NAME, functionName, compensationId);
                status = -2;
            } else {
                CompensationStructure updateCompensationStructure = compensation.get();
                updateCompensationStructure.setBaseAnnualSalary(compensationStructureDTO.baseAnnualSalary());
                updateCompensationStructure.setPaymentFrequency(compensationStructureDTO.paymentFrequency());
                updateCompensationStructure.setEffectiveDate(compensationStructureDTO.effectiveDate());
                updateCompensationStructure.setActive(compensationStructureDTO.isActive());
                updateCompensationStructure.setEpfEmployeeRate(compensationStructureDTO.epfEmployeeRate());
                updateCompensationStructure.setEpfEmployerRate(compensationStructureDTO.epfEmployerRate());
                updateCompensationStructure.setSocsoGroup(compensationStructureDTO.socsoGroup());
                updateCompensationStructure.setUpdatedAt(LocalDateTime.now());

                compensationStructureRepository.saveAndFlush(updateCompensationStructure);

                log.info("{} {} update success for compensationId={}", CLASS_NAME, functionName, compensationId);
                status = 1;
            }
        } catch (Exception e) {
            log.error("{} {} encountered exception. Message: {}", CLASS_NAME, functionName, e.getMessage());
            status = -1;
        }

        log.info("{} {} end. compensationId={}, status={}", CLASS_NAME, functionName, compensationId, status);
        return status;
    }

    @Override
    public int deleteEmployeeCompensationStructureById(long compensationId) {
        final String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("{} {} start. compensationId={}", CLASS_NAME, functionName, compensationId);

        int status = 0;

        Optional<CompensationStructure> compensation = compensationStructureRepository.findById(compensationId);
        if (compensation.isPresent()) {
            compensationStructureRepository.delete(compensation.get());

            log.info("{} {} delete success for compensationId={}", CLASS_NAME, functionName, compensationId);
            status = 1;
        } else {
            log.info("{} {} for compensationId={} not found.", CLASS_NAME, functionName, compensationId);
            status = -1;
        }

        log.info("{} {} end. compensationId={}, Status={}", CLASS_NAME, functionName, compensationId, status);
        return status;
    }

}
