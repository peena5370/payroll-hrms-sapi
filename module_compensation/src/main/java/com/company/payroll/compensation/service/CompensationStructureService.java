package com.company.payroll.compensation.service;

import com.company.payroll.compensation.dto.CompensationStructureDTO;
import com.company.payroll.compensation.dto.CompensationStructureDetailDTO;

public interface CompensationStructureService {

    void createEmployeeCompensationStructure(CompensationStructureDTO compensationStructureDTO);

    CompensationStructureDetailDTO getEmployeeCompensationStructureByEmployeeIdOrCompensationId(
            Long employeeId, Long compensationId);

    void updateEmployeeCompensationStructureById(long compensationId, CompensationStructureDTO compensationStructureDTO);

    void deleteEmployeeCompensationStructureById(long compensationId);
}
