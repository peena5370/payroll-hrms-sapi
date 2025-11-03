package com.company.payroll.department.dto;

public record DepartmentFacilityDetailDTO(
        Long departmentFUId,
        Long departmentId,
        Long facilityId,
        Long managerId,
        DepartmentDTO detail
) {
}
