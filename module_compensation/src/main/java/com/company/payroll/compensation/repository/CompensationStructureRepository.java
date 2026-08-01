package com.company.payroll.compensation.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.company.payroll.compensation.model.CompensationStructure;

public interface CompensationStructureRepository extends JpaRepository<CompensationStructure, Long> {

    boolean existsByEmployeeId(Long employeeId);

    Optional<CompensationStructure> findByEmployeeId(Long employeeId); // if this one i want to make it to chain for orElseThrow, then how
}
