package com.company.payroll.resignation.dto;

import java.time.Instant;

public record EmployeeResignationDetailDTO(
        Long resignationId,
        Instant createdAt,
        EmployeeResignationDTO detail
) {
}
