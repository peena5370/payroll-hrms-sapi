package com.company.payroll.compensation.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.company.payroll.compensation.model.CompensationStructure;

public interface CompensationStructureRepository extends JpaRepository<CompensationStructure, Long> {

    @Query("SELECT cs FROM CompensationStructure cs WHERE cs.employeeId = :employeeId")
    Optional<CompensationStructure> findByEmployeeId(@Param("employeeId") Long employeeId);
}
