package com.company.payroll.department.service;

import com.company.payroll.department.dto.DepartmentEmployeeDTO;
import com.company.payroll.department.dto.DepartmentEmployeeDetailDTO;

import java.util.List;

public interface DepartmentEmployeeService {
    void createDepartmentEmployeeInfo(DepartmentEmployeeDTO departmentEmployeeDTO);

    List<DepartmentEmployeeDetailDTO> getAllDepartmentEmployeeInfoByOffsetLimit(int offset, int limit);

    DepartmentEmployeeDetailDTO getDepartmentEmployeeInfoByDepartmentEid(long departmentEid);

    void updateDepartmentEmployeeInfoById(long departmentEid, DepartmentEmployeeDTO departmentEmployeeDTO);

    void deleteDepartmentEmployeeInfoById(long departmentEid);
}
