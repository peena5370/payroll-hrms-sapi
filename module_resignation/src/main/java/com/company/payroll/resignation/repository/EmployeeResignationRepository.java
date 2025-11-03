package com.company.payroll.resignation.repository;

import com.company.payroll.resignation.model.EmployeeResignation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeResignationRepository extends JpaRepository<EmployeeResignation, Long> {

    @Query("SELECT er FROM EmployeeResignation er WHERE er.employeeId = :employeeId")
    List<EmployeeResignation> findAllByEmployeeId(@Param("employeeId") Long employeeId);
}
