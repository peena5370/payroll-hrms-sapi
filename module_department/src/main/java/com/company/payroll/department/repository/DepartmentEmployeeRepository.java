package com.company.payroll.department.repository;

import com.company.payroll.department.model.DepartmentEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DepartmentEmployeeRepository extends JpaRepository<DepartmentEmployee, Long> {

        boolean existsByDepartmentEid(Long departmentEid);

        @Query("SELECT de FROM DepartmentEmployee de WHERE de.departmentId = :departmentId")
        List<DepartmentEmployee> getAllByDepartmentId(@Param("departmentId") Long departmentId);

        @Query("SELECT de FROM DepartmentEmployee de " +
                        "WHERE de.departmentId IN :departmentIds " +
                        "AND de.departmentFUId IN :departmentFUIds " +
                        "AND de.primary = :primary " +
                        "AND de.manager = :manager")
        List<DepartmentEmployee> getAllByDepartmentIdsByDepartmentFacilityUnitIdsAndIsPrimaryAndIsManager(
                        @Param("departmentIds") List<Long> departmentIds,
                        @Param("departmentFUIds") List<Long> departmentFUIds,
                        @Param("primary") boolean primary,
                        @Param("manager") boolean manager);

        @Query("SELECT de FROM DepartmentEmployee de WHERE de.employeeId = :employeeId")
        List<DepartmentEmployee> getAllByDepartmentByEmployeeId(@Param("employeeId") Long employeeId);

        @Query("SELECT de.departmentEid FROM DepartmentEmployee de WHERE de.departmentId = :departmentId AND de.departmentFUId = :departmentFUId AND de.employeeId = :employeeId")
        Optional<Long> getDepartmentEidByDepartmentIdAndDeparmentFacilityUnitIdAndEmployeeId(
                        @Param("departmentId") Long departmentId,
                        @Param("departmentFUId") Long departmentFUId,
                        @Param("employeeId") Long employeeId);
}
