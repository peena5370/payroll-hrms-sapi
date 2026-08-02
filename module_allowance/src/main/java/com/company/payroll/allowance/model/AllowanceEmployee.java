package com.company.payroll.allowance.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

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
@Table(name = "allowance_employee")
public class AllowanceEmployee {

    @Id
    @Column(name = "allowance_eid")
    private Long allowanceEid;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "allowance_id", nullable = false)
    private Long allowanceId;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "formula")
    private String formula;

    @Column(name = "effective_start_date", nullable = false)
    private LocalDate effectiveStartDate;

    @Column(name = "effective_end_date")
    private LocalDate effectiveEndDate;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;
}
