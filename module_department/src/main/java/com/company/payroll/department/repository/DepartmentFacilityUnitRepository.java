package com.company.payroll.department.repository;

import com.company.payroll.department.model.DepartmentFacilityUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DepartmentFacilityUnitRepository extends JpaRepository<DepartmentFacilityUnit, Long> {

    @Query("SELECT dfu FROM DepartmentFacilityUnit dfu WHERE dfu.departmentId = :departmentId")
    List<DepartmentFacilityUnit> getAllByDepartmentId(@Param("departmentId") Long departmentId);

    @Query("SELECT dfu FROM DepartmentFacilityUnit dfu WHERE dfu.facilityId = :facilityId")
    List<DepartmentFacilityUnit> getAllByFacilityId(@Param("facilityId") Long facilityId);

    @Query("SELECT dfu FROM DepartmentFacilityUnit dfu WHERE dfu.departmentId = :departmentId AND dfu.facilityId = :facilityId")
    Optional<DepartmentFacilityUnit> getByDepartmentIdAndFacilityId(@Param("departmentId") Long departmentId,
                                                                    @Param("facilityId") Long facilityId);
}
