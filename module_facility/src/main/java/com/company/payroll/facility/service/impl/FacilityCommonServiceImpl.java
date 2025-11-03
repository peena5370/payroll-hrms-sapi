package com.company.payroll.facility.service.impl;

import com.company.payroll.common.FacilityCommonService;
import com.company.payroll.facility.model.CompanyFacility;
import com.company.payroll.facility.repository.CompanyFacilityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service("facilityCommonService")
public class FacilityCommonServiceImpl implements FacilityCommonService {
    public static final String CLASS_NAME = "[FacilityCommonServiceImpl]";
    private final CompanyFacilityRepository companyFacilityRepository;

    public FacilityCommonServiceImpl(CompanyFacilityRepository companyFacilityRepository) {
        this.companyFacilityRepository = companyFacilityRepository;
    }

    @Override
    public boolean isCompanyFacilityExist(long facilityId) {
        final String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("{} {} start. facilityId={}", CLASS_NAME, functionName, facilityId);

        Optional<CompanyFacility> facility = companyFacilityRepository.findById(facilityId);
        boolean isExist = facility.isPresent();

        log.info("{} {} end. facilityId={}, result={}", CLASS_NAME, functionName, facilityId, isExist);
        return isExist;
    }
}
