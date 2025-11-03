package com.company.payroll.promotion.repository;

import com.company.payroll.promotion.model.EmployeePromotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeePromotionRepository extends JpaRepository<EmployeePromotion, Long> {

    @Query("SELECT ep FROM EmployeePromotion ep WHERE ep.employeeId = :employeeId")
    List<EmployeePromotion> findAllByEmployeeId(@Param("employeeId") Long employeeId);
}
