package com.company.payroll.loan.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.company.payroll.loan.constant.LoanApprovalStatus;
import com.company.payroll.loan.constant.LoanRepaymentTerm;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "compensation_structure")
public class LoanApplication {

    @Id
    @Column(name = "loan_id")
    private long loanId;

    @Column(name = "employee_id", nullable = false)
    private long employeeId;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "repayment_term", nullable = false)
    private LoanRepaymentTerm repaymentTerm;

    @Column(name = "eligibility", nullable = false)
    private String eligibility;

    @Column(name = "approver_id")
    private Long approverId;

    @Column(name = "status")
    private LoanApprovalStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
