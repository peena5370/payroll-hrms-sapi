package com.company.payroll.common.service;

public interface DepartmentCommonService {
    boolean isFacilityUnitInUsed(long facilityId);

    boolean isDepartmentEmployeeExist(long employeeId);
}
