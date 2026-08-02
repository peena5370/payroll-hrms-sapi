package com.company.payroll.department.dto;

import java.time.Instant;

public record DepartmentInfoDTO(
        Long departmentId,
        Instant createdAt,
        DepartmentDTO detail) {
}
