package com.company.payroll.facility.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.company.payroll.common.service.DepartmentCommonService;
import com.company.payroll.facility.dto.CompanyFacilityDTO;
import com.company.payroll.facility.dto.CompanyFacilityDetailDTO;
import com.company.payroll.facility.model.CompanyFacility;
import com.company.payroll.facility.repository.CompanyFacilityRepository;
import com.company.payroll.facility.service.CompanyFacilityService;
import com.company.payroll.util.util.SnowFlakeIdGenerator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CompanyFacilityServiceImpl implements CompanyFacilityService {
    public static final String CLASS_NAME = "[CompanyFacilityServiceImpl]";
    private final CompanyFacilityRepository companyFacilityRepository;
    private final SnowFlakeIdGenerator snowFlakeIdGenerator;

    private final DepartmentCommonService departmentCommonService;

    public CompanyFacilityServiceImpl(CompanyFacilityRepository companyFacilityRepository,
                                      SnowFlakeIdGenerator snowFlakeIdGenerator,
                                      DepartmentCommonService departmentCommonService) {
        this.companyFacilityRepository = companyFacilityRepository;
        this.snowFlakeIdGenerator = snowFlakeIdGenerator;
        this.departmentCommonService = departmentCommonService;
    }

    @Override
    public int createCompanyFacilityDetail(CompanyFacilityDTO companyFacilityDTO) {
        final String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("{} {} start.", CLASS_NAME, functionName);

        int status = 0;

        try {
            Optional<Long> facilityId = companyFacilityRepository.findIdByFacilityName(companyFacilityDTO.facilityName());

            if (facilityId.isPresent()) {
                status = -2;
            } else {
                CompanyFacility companyFacility = new CompanyFacility();
                companyFacility.setFacilityId(snowFlakeIdGenerator.nextId());
                companyFacility.setFacilityName(companyFacilityDTO.facilityName());
                companyFacility.setAddressLine1(companyFacilityDTO.addressLine1());
                companyFacility.setAddressLine2(companyFacilityDTO.addressLine2());
                companyFacility.setCity(companyFacilityDTO.city());
                companyFacility.setStateProvince(companyFacilityDTO.stateProvince());
                companyFacility.setPostalCode(companyFacilityDTO.postalCode());
                companyFacility.setCountry(companyFacilityDTO.country());
                companyFacility.setContactPersonId(companyFacilityDTO.contactPersonId());
                companyFacility.setFacilityPhoneNumber(companyFacilityDTO.facilityPhoneNumber());
                companyFacility.setCreatedAt(LocalDateTime.now());

                companyFacilityRepository.saveAndFlush(companyFacility);

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
    public List<CompanyFacilityDetailDTO> getAllCompanyFacilityDetailByOffsetAndLimit(int offset, int limit) {
        final String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("{} {} start.", CLASS_NAME, functionName);

        List<CompanyFacilityDetailDTO> facilityDetailDTOList = new ArrayList<>();

        Sort sort = Sort.by("facilityId").ascending();
        PageRequest pageRequest = PageRequest.of(offset, limit, sort);

        List<CompanyFacility> facilities = companyFacilityRepository.findAll(pageRequest).getContent();

        if (!facilities.isEmpty()) {
            for (CompanyFacility facility : facilities) {
                CompanyFacilityDTO facilityDetail = new CompanyFacilityDTO(
                        facility.getFacilityName(),
                        facility.getAddressLine1(),
                        facility.getAddressLine2(),
                        facility.getCity(),
                        facility.getStateProvince(),
                        facility.getPostalCode(),
                        facility.getCountry(),
                        facility.getContactPersonId(),
                        facility.getFacilityPhoneNumber());

                CompanyFacilityDetailDTO facilityDetailDTO = new CompanyFacilityDetailDTO(
                        facility.getFacilityId(),
                        facility.getCreatedAt(),
                        facilityDetail
                );

                facilityDetailDTOList.add(facilityDetailDTO);
            }
        }

        log.info("{} {} end. Size={}", CLASS_NAME, functionName, facilityDetailDTOList.size());
        return facilityDetailDTOList;
    }

    @Override
    public Optional<CompanyFacilityDetailDTO> getCompanyFacilityDetailById(long facilityId) {
        final String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("{} {} start. facilityId={}", CLASS_NAME, functionName, facilityId);

        Optional<CompanyFacility> facility = companyFacilityRepository.findById(facilityId);

        if (facility.isPresent()) {
            CompanyFacility result = facility.get();

            CompanyFacilityDTO facilityDetail = new CompanyFacilityDTO(
                    result.getFacilityName(),
                    result.getAddressLine1(),
                    result.getAddressLine2(),
                    result.getCity(),
                    result.getStateProvince(),
                    result.getPostalCode(),
                    result.getCountry(),
                    result.getContactPersonId(),
                    result.getFacilityPhoneNumber());

            CompanyFacilityDetailDTO facilityDetailDTO = new CompanyFacilityDetailDTO(
                    result.getFacilityId(),
                    result.getCreatedAt(),
                    facilityDetail
            );

            log.info("{} {} success for facilityId={}.", CLASS_NAME, functionName, facilityId);
            return Optional.of(facilityDetailDTO);
        }

        log.info("{} {} fail. Company facility info with facilityId={} not exist.", CLASS_NAME, functionName, facilityId);
        return Optional.empty();
    }

    @Override
    public int updateCompanyFacilityDetailById(long facilityId, CompanyFacilityDTO companyFacilityDTO) {
        final String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("{} {} start. facilityId={}", CLASS_NAME, functionName, facilityId);

        int status = 0;

        try {
            Optional<CompanyFacility> companyFacility = companyFacilityRepository.findById(facilityId);

            if (companyFacility.isEmpty()) {
                log.info("{} {} not found for facilityId={}", CLASS_NAME, functionName, facilityId);
                status = -2;
            } else {
                // check for duplicate facility name value which clash with other data
                Optional<Long> existingFacilityId = companyFacilityRepository.findIdByFacilityName(companyFacilityDTO.facilityName());

                if ((existingFacilityId.isPresent()) && (facilityId != existingFacilityId.get())) {
                    return -3;
                } else {
                    CompanyFacility updateFacility = companyFacility.get();
                    updateFacility.setFacilityName(companyFacilityDTO.facilityName());
                    updateFacility.setAddressLine1(companyFacilityDTO.addressLine1());
                    updateFacility.setAddressLine2(companyFacilityDTO.addressLine2());
                    updateFacility.setCity(companyFacilityDTO.city());
                    updateFacility.setStateProvince(companyFacilityDTO.stateProvince());
                    updateFacility.setPostalCode(companyFacilityDTO.postalCode());
                    updateFacility.setCountry(companyFacilityDTO.country());
                    updateFacility.setContactPersonId(companyFacilityDTO.contactPersonId());
                    updateFacility.setFacilityPhoneNumber(companyFacilityDTO.facilityPhoneNumber());
                    updateFacility.setUpdatedAt(LocalDateTime.now());

                    companyFacilityRepository.saveAndFlush(updateFacility);

                    log.info("{} {} update success for facilityId={}", CLASS_NAME, functionName, facilityId);
                    status = 1;
                }
            }
        } catch (Exception e) {
            log.error("{} {} encountered exception. Message: {}", CLASS_NAME, functionName, e.getMessage());
            status = -1;
        }

        log.info("{} {} end. facilityId={}, status={}", CLASS_NAME, functionName, facilityId, status);
        return status;
    }

    @Override
    public int deleteCompanyFacilityDetailById(long facilityId) {
        final String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("{} {} start. facilityId={}", CLASS_NAME, functionName, facilityId);

        int status = 0;

        Optional<CompanyFacility> facility = companyFacilityRepository.findById(facilityId);
        if (facility.isPresent()) {
            boolean isInUsed = departmentCommonService.isFacilityUnitInUsed(facilityId);

            if(isInUsed) {
                log.info("{} {} for facilityId={} is in used, not allow to delete.", CLASS_NAME, functionName, facilityId);
                status = -2;
            } else {
                companyFacilityRepository.delete(facility.get());

                log.info("{} {} delete success for facilityId={}", CLASS_NAME, functionName, facilityId);
                status = 1;
            }
        } else {
            log.info("{} {} for facilityId={} not found.", CLASS_NAME, functionName, facilityId);
            status = -1;
        }

        log.info("{} {} end. facilityId={}, Status={}", CLASS_NAME, functionName, facilityId, status);
        return status;
    }
}
