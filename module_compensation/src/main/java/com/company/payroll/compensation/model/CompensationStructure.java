package com.company.payroll.compensation.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.company.payroll.compensation.constant.CompensationPaymentFrequency;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class CompensationStructure {

    @Id
    @Column(name = "compensation_id")
    private long compensationId;

    @Column(name = "employee_id", nullable = false)
    private long employeeId;

    @Column(name = "base_annual_salary", nullable = false)
    private double baseAnnualSalary;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_frequency", nullable = false)
    private CompensationPaymentFrequency paymentFrequency;

    @Column(name = "effective_date", nullable = false)
    private LocalDate effectiveDate;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Column(name = "epf_employee_rate")
    private Double epfEmployeeRate;

    @Column(name = "epf_employer_rate")
    private Double epfEmployerRate;

    @Column(name = "socso_group", nullable = false)
    private String socsoGroup;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
