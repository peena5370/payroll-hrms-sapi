package com.company.payroll.common.service;

import java.math.BigDecimal;

import com.company.payroll.common.constant.LoanEligibleStatus;

public interface EmployeeCommonService {

    LoanEligibleStatus getEmployeeLoanEligibility(long employeeId, BigDecimal loanAmount);
}
