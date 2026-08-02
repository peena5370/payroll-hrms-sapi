package com.company.payroll.department.repository;

import com.company.payroll.department.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    boolean existsByCostCenterCode(String costCenterCode);

    @Query("SELECT d.departmentId FROM Department d WHERE d.costCenterCode = :costCenterCode")
    Optional<Long> findIdByDepartmentCostCenterCode(@Param("costCenterCode") String costCenterCode);

    @Query("SELECT d FROM Department d WHERE d.departmentId In :departmentIds")
    List<Department> getAllByDepartmentIds(@Param("departmentIds") List<Long> departmentIds);
}
