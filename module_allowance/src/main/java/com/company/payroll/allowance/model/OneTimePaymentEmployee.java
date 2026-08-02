package com.company.payroll.allowance.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

import com.company.payroll.allowance.constant.PaymentStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "one_time_payment_employee")
public class OneTimePaymentEmployee {

    @Id
    @Column(name = "payment_eid")
    private Long paymentEid;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "payment_type_id", nullable = false)
    private Long paymentTypeId;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "approval_date")
    private Instant approvalDate;

    @Column(name = "scheduled_payment_date", nullable = false)
    private LocalDate schedulePaymentDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;
}
