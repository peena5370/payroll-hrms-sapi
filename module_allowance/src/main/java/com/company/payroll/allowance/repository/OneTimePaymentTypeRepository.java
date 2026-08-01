package com.company.payroll.allowance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.company.payroll.allowance.model.OneTimePaymentType;

public interface OneTimePaymentTypeRepository extends JpaRepository<OneTimePaymentType, Long> {

}
