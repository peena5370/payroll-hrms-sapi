package com.company.payroll.employee.service;

import com.company.payroll.employee.dto.EmployeeDTO;
import com.company.payroll.employee.dto.EmployeeInfoDTO;

import java.util.List;

public interface EmployeeService {

    void createEmployeeInfo(EmployeeDTO employeeDTO);

    List<EmployeeInfoDTO> getAllEmployeesByOffsetAndLimit(int offset, int limit);

    EmployeeInfoDTO getEmployeeInfoById(long employeeId);

    void updateEmployeeInfoById(long employeeId, EmployeeDTO employeeDTO);

    void deleteEmployeeInfoById(long employeeId);
}
