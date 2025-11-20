package com.company.payroll.common.service;

import java.math.BigDecimal;

import com.company.payroll.common.constant.LoanEligibleStatus;

public interface CompensationCommonService {

    LoanEligibleStatus getEmployeeCompensationLoanEligibility(long employeeId, BigDecimal loanAmount, String repaymentTerm);
}
