package com.company.payroll.allowance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.company.payroll.allowance.model.AllowanceType;

public interface AllowanceTypeRepository extends JpaRepository<AllowanceType, Long> {

}
