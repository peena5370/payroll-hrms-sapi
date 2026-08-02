package com.company.payroll.department.dto;

import java.time.LocalDate;

public record DepartmentEmployeeDTO(
        Long departmentId,
        Long departmentFUId,
        Long employeeId,
        Boolean isPrimary,
        Boolean isManager,
        LocalDate joinedAt,
        LocalDate leavedAt
) {
}
