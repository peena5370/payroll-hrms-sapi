package com.company.payroll.allowance.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.company.payroll.allowance.constant.PaymentStatus;

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
@Table(name = "one_time_payment_employee")
public class OneTimePaymentEmployee {

    @Id
    @Column(name = "payment_eid")
    private long paymentEid;

    @Column(name = "employee_id", nullable = false)
    private long employeeId;

    @Column(name = "payment_type_id", nullable = false)
    private long paymentTypeId;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "approval_date")
    private LocalDateTime approvalDate;

    @Column(name = "scheduled_payment_date", nullable = false)
    private LocalDate schedulePaymentDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
