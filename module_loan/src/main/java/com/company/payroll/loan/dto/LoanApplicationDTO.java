package com.company.payroll.loan.dto;

import java.math.BigDecimal;

import com.company.payroll.loan.constant.LoanRepaymentTerm;

public record LoanApplicationDTO(
    Long employeeId,
    BigDecimal amount,
    LoanRepaymentTerm repaymentTerm
) {

}
