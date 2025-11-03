package com.company.payroll.department.dto;

import java.time.LocalDate;

public record DepartmentEmployeeDTO(
        Long departmentId,
        Long departmentFUId,
        Long employeeId,
        boolean isPrimary,
        boolean isManager,
        LocalDate joinedAt,
        LocalDate leavedAt
) {
}
