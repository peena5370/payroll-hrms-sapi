package com.company.payroll.resignation.service.impl;

import com.company.payroll.resignation.dto.EmployeeResignationDTO;
import com.company.payroll.resignation.dto.EmployeeResignationDetailDTO;
import com.company.payroll.resignation.model.EmployeeResignation;
import com.company.payroll.resignation.repository.EmployeeResignationRepository;
import com.company.payroll.resignation.service.EmployeeResignationService;
import com.company.payroll.util.util.SnowFlakeIdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class EmployeeResignationServiceImpl implements EmployeeResignationService {
    private static final String CLASS_NAME = "[EmployeeResignationServiceImpl]";

    private final SnowFlakeIdGenerator snowFlakeIdGenerator;
    private final EmployeeResignationRepository employeeResignationRepository;

    public EmployeeResignationServiceImpl(SnowFlakeIdGenerator snowFlakeIdGenerator,
                                          EmployeeResignationRepository employeeResignationRepository) {
        this.snowFlakeIdGenerator = snowFlakeIdGenerator;
        this.employeeResignationRepository = employeeResignationRepository;
    }

    @Override
    public int createResignationInfo(EmployeeResignationDTO employeeResignationDTO) {
        final String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("{} {} start.", CLASS_NAME, functionName);

        int status = 0;
        try {
            EmployeeResignation resignation = new EmployeeResignation(
                    snowFlakeIdGenerator.nextId(),
                    employeeResignationDTO.employeeId(),
                    employeeResignationDTO.resignDate(),
                    employeeResignationDTO.lastServiceDate(),
                    employeeResignationDTO.reason(),
                    employeeResignationDTO.noticePeriod(),
                    employeeResignationDTO.isExitInterviewConducted(),
                    employeeResignationDTO.exitInterviewNote(),
                    employeeResignationDTO.status(),
                    employeeResignationDTO.approverId(),
                    LocalDateTime.now(),
                    null
            );

            employeeResignationRepository.saveAndFlush(resignation);

            log.info("{} {} create success.", CLASS_NAME, functionName);
            status = 1;
        } catch (Exception e) {
            log.error("{} {} encountered exception. Message: {}", CLASS_NAME, functionName, e.getMessage());
            status = -1;
        }

        log.info("{} {} end. Status={}", CLASS_NAME, functionName, status);
        return status;
    }

    @Override
    public List<EmployeeResignationDetailDTO> getAllResignationInfoByOffsetAndLimitOrEmployeeId(Long employeeId, int offset, int limit) {
        final String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("{} {} start.", CLASS_NAME, functionName);

        List<EmployeeResignationDetailDTO> employeeResignationDetailDTOList = new ArrayList<>();

        List<EmployeeResignation> resignations = null;

        Sort sort = Sort.by("resignationId").ascending();
        PageRequest pageRequest = PageRequest.of(offset, limit, sort);

        if(employeeId != null) {
            resignations = employeeResignationRepository.findAllByEmployeeId(employeeId);
        } else {
            resignations = employeeResignationRepository.findAll(pageRequest).getContent();
        }

        if ((resignations != null) && (!resignations.isEmpty())) {
            for (EmployeeResignation resignation : resignations) {
                EmployeeResignationDTO detail = new EmployeeResignationDTO(
                        resignation.getEmployeeId(),
                        resignation.getResignationDate(),
                        resignation.getLastWorkingDay(),
                        resignation.getResignationReason(),
                        resignation.getNoticePeriodDays(),
                        resignation.isExitInterviewConducted(),
                        resignation.getExitInterviewNote(),
                        resignation.getStatus(),
                        resignation.getApprovedById());

                EmployeeResignationDetailDTO resignationDetailDTO = new EmployeeResignationDetailDTO(
                        resignation.getResignationId(),
                        resignation.getCreatedAt(),
                        detail
                );

                employeeResignationDetailDTOList.add(resignationDetailDTO);
            }
        }

        log.info("{} {} end. Size={}", CLASS_NAME, functionName, employeeResignationDetailDTOList.size());
        return employeeResignationDetailDTOList;
    }

    @Override
    public Optional<EmployeeResignationDetailDTO> getResignationInfoById(long resignationId) {
        final String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("{} {} start. resignationId={}", CLASS_NAME, functionName, resignationId);

        Optional<EmployeeResignation> resignation = employeeResignationRepository.findById(resignationId);

        if (resignation.isPresent()) {
            EmployeeResignation result = resignation.get();

            EmployeeResignationDTO detail = new EmployeeResignationDTO(
                    result.getEmployeeId(),
                    result.getResignationDate(),
                    result.getLastWorkingDay(),
                    result.getResignationReason(),
                    result.getNoticePeriodDays(),
                    result.isExitInterviewConducted(),
                    result.getExitInterviewNote(),
                    result.getStatus(),
                    result.getApprovedById());

            EmployeeResignationDetailDTO resignationDetailDTO = new EmployeeResignationDetailDTO(
                    result.getResignationId(),
                    result.getCreatedAt(),
                    detail
            );

            log.info("{} {} success for resignationId={}.", CLASS_NAME, functionName, resignationId);
            return Optional.of(resignationDetailDTO);
        }

        log.info("{} {} fail. Employee promotion info with resignationId={} not exist.", CLASS_NAME, functionName, resignationId);
        return Optional.empty();
    }

    @Override
    public int updateResignationInfoById(long resignationId, EmployeeResignationDTO employeeResignationDTO) {
        final String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("{} {} start. resignationId={}", CLASS_NAME, functionName, resignationId);

        int status = 0;

        try {
            Optional<EmployeeResignation> resignation = employeeResignationRepository.findById(resignationId);

            if (resignation.isEmpty()) {
                log.info("{} {} not found for resignationId={}", CLASS_NAME, functionName, resignationId);
                status = -2;
            } else {
                EmployeeResignation updateResignation = resignation.get();
                updateResignation.setEmployeeId(employeeResignationDTO.employeeId());
                updateResignation.setResignationDate(employeeResignationDTO.resignDate());
                updateResignation.setLastWorkingDay(employeeResignationDTO.lastServiceDate());
                updateResignation.setResignationReason(employeeResignationDTO.reason());
                updateResignation.setNoticePeriodDays(employeeResignationDTO.noticePeriod());
                updateResignation.setExitInterviewConducted(employeeResignationDTO.isExitInterviewConducted());
                updateResignation.setExitInterviewNote(employeeResignationDTO.exitInterviewNote());
                updateResignation.setStatus(employeeResignationDTO.status());
                updateResignation.setApprovedById(employeeResignationDTO.approverId());
                updateResignation.setUpdatedAt(LocalDateTime.now());

                employeeResignationRepository.saveAndFlush(updateResignation);

                log.info("{} {} update success for resignationId={}", CLASS_NAME, functionName, resignationId);
                status = 1;
            }
        } catch (Exception e) {
            log.error("{} {} encountered exception. Message: {}", CLASS_NAME, functionName, e.getMessage());
            status = -1;
        }

        log.info("{} {} end. resignationId={}, status={}", CLASS_NAME, functionName, resignationId, status);
        return status;
    }

    @Override
    public int deleteResignationInfoById(long resignationId) {
        final String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("{} {} start. resignationId={}", CLASS_NAME, functionName, resignationId);

        int status = 0;

        Optional<EmployeeResignation> resignation = employeeResignationRepository.findById(resignationId);
        if (resignation.isPresent()) {
            employeeResignationRepository.delete(resignation.get());

            log.info("{} {} delete success for resignationId={}", CLASS_NAME, functionName, resignationId);
            status = 1;
        } else {
            log.info("{} {} for resignationId={} not found.", CLASS_NAME, functionName, resignationId);
            status = -1;
        }

        log.info("{} {} end. resignationId={}, Status={}", CLASS_NAME, functionName, resignationId, status);
        return status;
    }
}
