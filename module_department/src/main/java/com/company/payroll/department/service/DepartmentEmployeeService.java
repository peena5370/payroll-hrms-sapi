package com.company.payroll.department.service;

import com.company.payroll.department.dto.DepartmentEmployeeDTO;
import com.company.payroll.department.dto.DepartmentEmployeeDetailDTO;

import java.util.List;
import java.util.Optional;

public interface DepartmentEmployeeService {
    int createDepartmentEmployeeInfo(DepartmentEmployeeDTO departmentEmployeeDTO);

    List<DepartmentEmployeeDetailDTO> getAllDepartmentEmployeeInfoByOffsetLimit(int offset, int limit);

    Optional<DepartmentEmployeeDetailDTO> getDepartmentEmployeeInfoByDepartmentEid(long departmentEid);

    int updateDepartmentEmployeeInfoById(long departmentEid, DepartmentEmployeeDTO departmentEmployeeDTO);

    int deleteDepartmentEmployeeInfoById(long departmentEid);
}
