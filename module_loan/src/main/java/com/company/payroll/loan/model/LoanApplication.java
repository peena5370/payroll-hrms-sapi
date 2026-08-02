package com.company.payroll.loan.model;

import java.math.BigDecimal;
import java.time.Instant;

import com.company.payroll.loan.constant.LoanApprovalStatus;
import com.company.payroll.loan.constant.LoanRepaymentTerm;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "compensation_structure")
public class LoanApplication {

    @Id
    @Column(name = "loan_id")
    private Long loanId;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

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
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;
}
