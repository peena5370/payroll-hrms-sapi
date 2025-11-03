package com.company.payroll.promotion.service.impl;

import com.company.payroll.promotion.dto.EmployeePromotionDTO;
import com.company.payroll.promotion.dto.EmployeePromotionDetailDTO;
import com.company.payroll.promotion.model.EmployeePromotion;
import com.company.payroll.promotion.repository.EmployeePromotionRepository;
import com.company.payroll.promotion.service.EmployeePromotionService;
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
public class EmployeePromotionServiceImpl implements EmployeePromotionService {
    private static final String CLASS_NAME = "[EmployeePromotionServiceImpl]";

    private final SnowFlakeIdGenerator snowFlakeIdGenerator;
    private final EmployeePromotionRepository employeePromotionRepository;

    public EmployeePromotionServiceImpl(SnowFlakeIdGenerator snowFlakeIdGenerator,
                                        EmployeePromotionRepository employeePromotionRepository) {
        this.snowFlakeIdGenerator = snowFlakeIdGenerator;
        this.employeePromotionRepository = employeePromotionRepository;
    }

    @Override
    public int createPromotionDetail(EmployeePromotionDTO employeePromotionDTO) {
        final String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("{} {} start.", CLASS_NAME, functionName);

        int status = 0;
        try {
            EmployeePromotion promotion = new EmployeePromotion(
                    snowFlakeIdGenerator.nextId(),
                    employeePromotionDTO.employeeId(),
                    employeePromotionDTO.oldJobTitle(),
                    employeePromotionDTO.newJobTitle(),
                    employeePromotionDTO.oldDepartmentId(),
                    employeePromotionDTO.newDepartmentId(),
                    employeePromotionDTO.promoteDate(),
                    employeePromotionDTO.incrementAmount(),
                    employeePromotionDTO.reason(),
                    employeePromotionDTO.approverId(),
                    LocalDateTime.now(),
                    null
            );

            employeePromotionRepository.saveAndFlush(promotion);

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
    public List<EmployeePromotionDetailDTO> getAllPromotionsByOffsetAndLimitOrEmployeeId(Long employeeId, int offset, int limit) {
        final String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("{} {} start.", CLASS_NAME, functionName);

        List<EmployeePromotionDetailDTO> employeePromotionDetailDTOList = new ArrayList<>();

        List<EmployeePromotion> promotions = null;

        Sort sort = Sort.by("promotionId").ascending();
        PageRequest pageRequest = PageRequest.of(offset, limit, sort);

        if(employeeId != null) {
            promotions = employeePromotionRepository.findAllByEmployeeId(employeeId);
        } else {
            promotions = employeePromotionRepository.findAll(pageRequest).getContent();
        }

        if ((promotions != null) && (!promotions.isEmpty())) {
            for (EmployeePromotion promotion : promotions) {
                EmployeePromotionDTO detail = new EmployeePromotionDTO(
                        promotion.getEmployeeId(),
                        promotion.getOldJobTitle(),
                        promotion.getNewJobTitle(),
                        promotion.getOldDepartmentId(),
                        promotion.getNewDepartmentId(),
                        promotion.getPromotionDate(),
                        promotion.getSalaryIncrementAmount(),
                        promotion.getPromotionReason(),
                        promotion.getApprovedById());

                EmployeePromotionDetailDTO promotionDetailDTO = new EmployeePromotionDetailDTO(
                        promotion.getPromotionId(),
                        promotion.getCreatedAt(),
                        detail
                );

                employeePromotionDetailDTOList.add(promotionDetailDTO);
            }
        }

        log.info("{} {} end. Size={}", CLASS_NAME, functionName, employeePromotionDetailDTOList.size());
        return employeePromotionDetailDTOList;
    }

    @Override
    public Optional<EmployeePromotionDetailDTO> getPromotionDetailById(long promotionId) {
        final String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();

        log.info("{} {} start. promotionId={}", CLASS_NAME, functionName, promotionId);

        Optional<EmployeePromotion> promotion = employeePromotionRepository.findById(promotionId);

        if (promotion.isPresent()) {
            EmployeePromotion result = promotion.get();

            EmployeePromotionDTO detail = new EmployeePromotionDTO(
                    result.getEmployeeId(),
                    result.getOldJobTitle(),
                    result.getNewJobTitle(),
                    result.getOldDepartmentId(),
                    result.getNewDepartmentId(),
                    result.getPromotionDate(),
                    result.getSalaryIncrementAmount(),
                    result.getPromotionReason(),
                    result.getApprovedById());

            EmployeePromotionDetailDTO promotionDetailDTO = new EmployeePromotionDetailDTO(
                    result.getPromotionId(),
                    result.getCreatedAt(),
                    detail
            );

            log.info("{} {} success for promotionId={}.", CLASS_NAME, functionName, promotionId);
            return Optional.of(promotionDetailDTO);
        }

        log.info("{} {} fail. Employee promotion info with promotionId={} not exist.", CLASS_NAME, functionName, promotionId);
        return Optional.empty();
    }

    @Override
    public int updatePromotionDetailById(long promotionId, EmployeePromotionDTO employeePromotionDTO) {
        final String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("{} {} start. promotionId={}", CLASS_NAME, functionName, promotionId);

        int status = 0;

        try {
            Optional<EmployeePromotion> promotion = employeePromotionRepository.findById(promotionId);

            if (promotion.isEmpty()) {
                log.info("{} {} not found for promotionId={}", CLASS_NAME, functionName, promotionId);
                status = -2;
            } else {
                EmployeePromotion updatePromotion = promotion.get();
                updatePromotion.setEmployeeId(employeePromotionDTO.employeeId());
                updatePromotion.setOldJobTitle(employeePromotionDTO.oldJobTitle());
                updatePromotion.setNewJobTitle(employeePromotionDTO.newJobTitle());
                updatePromotion.setOldDepartmentId(employeePromotionDTO.oldDepartmentId());
                updatePromotion.setNewDepartmentId(employeePromotionDTO.newDepartmentId());
                updatePromotion.setPromotionDate(employeePromotionDTO.promoteDate());
                updatePromotion.setSalaryIncrementAmount(employeePromotionDTO.incrementAmount());
                updatePromotion.setPromotionReason(employeePromotionDTO.reason());
                updatePromotion.setApprovedById(employeePromotionDTO.approverId());
                updatePromotion.setUpdatedAt(LocalDateTime.now());

                employeePromotionRepository.saveAndFlush(updatePromotion);

                log.info("{} {} update success for promotionId={}", CLASS_NAME, functionName, promotionId);
                status = 1;
            }
        } catch (Exception e) {
            log.error("{} {} encountered exception. Message: {}", CLASS_NAME, functionName, e.getMessage());
            status = -1;
        }

        log.info("{} {} end. promotionId={}, status={}", CLASS_NAME, functionName, promotionId, status);
        return status;
    }

    @Override
    public int deletePromotionDetailById(long promotionId) {
        final String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("{} {} start. promotionId={}", CLASS_NAME, functionName, promotionId);

        int status = 0;

        Optional<EmployeePromotion> promotion = employeePromotionRepository.findById(promotionId);
        if (promotion.isPresent()) {
            employeePromotionRepository.delete(promotion.get());

            log.info("{} {} delete success for promotionId={}", CLASS_NAME, functionName, promotionId);
            status = 1;
        } else {
            log.info("{} {} for promotionId={} not found.", CLASS_NAME, functionName, promotionId);
            status = -1;
        }

        log.info("{} {} end. promotionId={}, Status={}", CLASS_NAME, functionName, promotionId, status);
        return status;
    }
}
