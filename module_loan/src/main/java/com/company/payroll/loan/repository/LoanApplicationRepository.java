package com.company.payroll.loan.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.company.payroll.loan.model.LoanApplication;

public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Long> {

}
