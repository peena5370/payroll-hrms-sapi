package com.company.payroll.employee.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.company.payroll.common.constant.LoanEligibleStatus;
import com.company.payroll.common.service.EmployeeCommonService;
import com.company.payroll.employee.model.Employee;
import com.company.payroll.employee.repository.EmployeeRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("employeeCommonService")
public class EmployeeCommonServiceImpl implements EmployeeCommonService {

    private static final String CLASS_NAME = "[EmployeeCommonServiceImpl]";

    private final EmployeeRepository employeeRepository;

    private int calculateTenureInYears(LocalDate hiredDate) {
        if (hiredDate == null) {
            return 0;
        }

        LocalDate today = LocalDate.now();

        Period period = Period.between(hiredDate, today);

        return period.getYears();
    }

    private boolean isEligibleByTitleAndAmount(String jobTitle, BigDecimal loanAmount) {
        boolean isEligible = false;

        BigDecimal loanThresholdAmount = new BigDecimal(5000);

        if (jobTitle != null) {
            String normalizedTitle = jobTitle.toUpperCase();

            // #1 if the loan amount below 5k, and title is engineer or executive, then is
            // eligible to apply loan
            // #2 if the loan amount is above 5k and title is manager or director, then is
            // eligible for the loan
            if ((loanAmount.compareTo(loanThresholdAmount) < 0)
                    && (normalizedTitle.contains("ENGINEER") || normalizedTitle.contains("EXECUTIVE"))) {
                isEligible = true;
            } else if ((loanAmount.compareTo(loanThresholdAmount) > 0)
                    && (normalizedTitle.contains("MANAGER") || normalizedTitle.contains("DIRECTOR"))) {
                isEligible = true;
            }
        }

        return isEligible;
    }

    public EmployeeCommonServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public LoanEligibleStatus getEmployeeLoanEligibility(long employeeId, BigDecimal loanAmount) {
        final String functionName = Thread.currentThread().getStackTrace()[2].getMethodName();
        log.info("{} {} start. employeeId={}", CLASS_NAME, functionName, employeeId);

        LoanEligibleStatus loanResult = LoanEligibleStatus.NOT_ELIGIBLE;

        Optional<Employee> employee = employeeRepository.findById(employeeId);

        if (employee.isPresent()) {
            Employee result = employee.get();

            // check eligiblity in job position
            boolean isPositionEligible = isEligibleByTitleAndAmount(result.getJobTitle(), loanAmount);

            if (isPositionEligible) {
                // check whether the tenure is mature or not
                if (calculateTenureInYears(result.getHireDate()) > 1) {
                    loanResult = LoanEligibleStatus.TENURE_MET;
                } else {
                    loanResult = LoanEligibleStatus.TENURE_INSUFFICIENT;
                }
            }
        }

        log.info("{} {} end. employeeId={}, status={}", CLASS_NAME, functionName, employeeId, loanResult);
        return loanResult;
    }

}
