package com.company.payroll.allowance.model;

import java.time.Instant;

import com.company.payroll.allowance.constant.RecurrenceType;

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
@Table(name = "allowance_type")
public class AllowanceType {

    @Id
    @Column(name = "allowance_id")
    private Long allowanceId;

    @Column(name = "type_name", nullable = false)
    private String typeName;

    @Column(name = "description")
    private String description;

    @Column(name = "is_taxable", nullable = false)
    private Boolean taxable;

    @Enumerated(EnumType.STRING)
    @Column(name = "recurrence", nullable = false)
    private RecurrenceType recurrence;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;
}
