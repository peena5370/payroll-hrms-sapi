package com.company.payroll.department.service;

import com.company.payroll.department.dto.DepartmentDTO;
import com.company.payroll.department.dto.DepartmentInfoDTO;

import java.util.List;

public interface DepartmentService {

    List<DepartmentInfoDTO> getAllDepartmentInfoByOffsetLimit(int offset, int limit);

    DepartmentInfoDTO getDepartmentInfoByDepartmentId(long departmentId);

    void createDepartmentInfo(DepartmentDTO departmentDTO);

    void updateDepartmentInfoById(long departmentId, DepartmentDTO departmentDTO);

    void deleteDepartmentInfoById(long departmentId);
}
