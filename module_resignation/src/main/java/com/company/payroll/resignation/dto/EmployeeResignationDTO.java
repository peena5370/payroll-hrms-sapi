package com.company.payroll.resignation.dto;

import java.time.LocalDate;

public record EmployeeResignationDTO(
        Long employeeId,
        LocalDate resignDate,
        LocalDate lastServiceDate,
        String reason,
        Integer noticePeriod,
        boolean isExitInterviewConducted,
        String exitInterviewNote,
        String status,
        Long approverId
) {
}
