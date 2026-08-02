package com.company.payroll.employee.repository;

import com.company.payroll.employee.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    boolean existsByIcNumber(String icNumber);
    
    @Query("SELECT e.employeeId FROM Employee e WHERE e.icNumber = :icNumber")
    Optional<Long> findIdByIcNumber(@Param("icNumber") String icNumber);
}
