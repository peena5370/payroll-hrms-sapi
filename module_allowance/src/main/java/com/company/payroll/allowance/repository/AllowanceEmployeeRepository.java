package com.company.payroll.allowance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.company.payroll.allowance.model.AllowanceEmployee;

public interface AllowanceEmployeeRepository extends JpaRepository<AllowanceEmployee, Long> {

}
