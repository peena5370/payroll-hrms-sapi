package com.company.payroll.employee.dto;

import java.time.Instant;

public record EmployeeInfoDTO(
        Long employeeId,
        Instant createdAt,
        EmployeeDTO detail
) {
}
