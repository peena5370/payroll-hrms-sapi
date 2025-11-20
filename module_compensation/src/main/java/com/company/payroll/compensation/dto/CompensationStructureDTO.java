package com.company.payroll.compensation.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.company.payroll.compensation.constant.CompensationPaymentFrequency;

public record CompensationStructureDTO(
    Long employeeId,
    BigDecimal baseAnnualSalary,
    CompensationPaymentFrequency paymentFrequency,
    LocalDate effectiveDate,
    boolean isActive,
    BigDecimal epfEmployeeRate,
    BigDecimal epfEmployerRate,
    String socsoGroup
) {

}
