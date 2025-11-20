package com.company.payroll.allowance.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
@Table(name = "allowance_employee")
public class AllowanceEmployee {

    @Id
    @Column(name = "allowance_eid")
    private long allowanceEid;

    @Column(name = "employee_id", nullable = false)
    private long employeeId;

    @Column(name = "allowance_id", nullable = false)
    private long allowanceId;

    @Column(name = "amount", nullable = false)
    private double amount;

    @Column(name = "formula")
    private String formula;

    @Column(name = "effective_start_date", nullable = false)
    private LocalDate effectiveStartDate;

    @Column(name = "effective_end_date")
    private LocalDate effectiveEndDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
