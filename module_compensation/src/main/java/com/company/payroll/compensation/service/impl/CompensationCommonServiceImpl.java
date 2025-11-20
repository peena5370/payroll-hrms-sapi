package com.company.payroll.compensation.service.impl;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.company.payroll.common.constant.LoanEligibleStatus;
import com.company.payroll.common.service.CompensationCommonService;
import com.company.payroll.compensation.model.CompensationStructure;
import com.company.payroll.compensation.repository.CompensationStructureRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("compensationCommonService")
public class CompensationCommonServiceImpl implements CompensationCommonService {

    private static final String CLASS_NAME = "[CompensationCommonServiceImpl]";

    private CompensationStructureRepository compensationStructureRepository;

    public CompensationCommonServiceImpl(CompensationStructureRepository compensationStructureRepository) {
        this.compensationStructureRepository = compensationStructureRepository;
    }

    @Override
    public LoanEligibleStatus getEmployeeCompensationLoanEligibility(long employeeId, BigDecimal loanAmount,
            String repaymentTerm) {
        final String functionName = Thread.currentThread().getStackTrace()[2].getMethodName();
        log.info("{} {} start. employeeId={}", CLASS_NAME, functionName, employeeId);

        LoanEligibleStatus finalResult = LoanEligibleStatus.NOT_ELIGIBLE;

        Optional<CompensationStructure> compensation = compensationStructureRepository.findByEmployeeId(employeeId);
        
        if(compensation.isPresent()) {

        }

        log.info("{} {} end. employeeId={}, status={}", CLASS_NAME, functionName, employeeId, finalResult);
        return finalResult;
    }

}
