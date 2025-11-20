package com.company.payroll.compensation.service;

import java.util.Optional;

import com.company.payroll.compensation.dto.CompensationStructureDTO;
import com.company.payroll.compensation.dto.CompensationStructureDetailDTO;

public interface CompensationStructureService {

    int createEmployeeCompensationStructure(CompensationStructureDTO compensationStructureDTO);

    Optional<CompensationStructureDetailDTO> getEmployeeCompensationStructureByEmployeeIdOrCompensationId(
            Long employeeId, Long compensationId);

    int updateEmployeeCompensationStructureById(long compensationId, CompensationStructureDTO compensationStructureDTO);

    int deleteEmployeeCompensationStructureById(long compensationId);
}
