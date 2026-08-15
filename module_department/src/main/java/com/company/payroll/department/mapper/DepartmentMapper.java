package com.company.payroll.department.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.company.payroll.department.dto.DepartmentDTO;
import com.company.payroll.department.dto.DepartmentInfoDTO;
import com.company.payroll.department.model.Department;

@Mapper
public interface DepartmentMapper {

    DepartmentDTO toDepartmentDTO(Department department);

    @Mapping(target = "departmentId", source = "department.departmentId")
    @Mapping(target = "createdAt", source = "department.createdAt")
    @Mapping(target = "detail", source = "department")
    DepartmentInfoDTO toDepartmentInfoDTO(Department department);
}
