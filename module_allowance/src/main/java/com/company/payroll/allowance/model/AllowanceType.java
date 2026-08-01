package com.company.payroll.allowance.model;

import java.time.LocalDateTime;

import com.company.payroll.allowance.constant.RecurrenceType;

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
@Table(name = "allowance_type")
public class AllowanceType {

    @Id
    @Column(name = "allowance_id")
    private long allowanceId;

    @Column(name = "type_name", nullable = false)
    private String typeName;

    @Column(name = "description")
    private String description;

    @Column(name = "is_taxable", nullable = false)
    private boolean is_taxable;

    @Enumerated(EnumType.STRING)
    @Column(name = "recurrence", nullable = false)
    private RecurrenceType recurrence;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
