package com.company.payroll.compensation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.company.payroll.compensation.model.CompensationStructure;

public interface CompensationStructureRepository extends JpaRepository<CompensationStructure, Long> {

}
