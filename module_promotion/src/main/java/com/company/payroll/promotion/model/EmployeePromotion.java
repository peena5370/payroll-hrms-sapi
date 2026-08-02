package com.company.payroll.promotion.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "employee_promotion")
public class EmployeePromotion {
    @Id
    @Column(name = "promotion_id")
    private Long promotionId;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "old_job_title", nullable = false)
    private String oldJobTitle;

    @Column(name = "new_job_title", nullable = false)
    private String newJobTitle;

    @Column(name = "old_department_id", nullable = false)
    private Long oldDepartmentId;

    @Column(name = "new_department_id", nullable = false)
    private Long newDepartmentId;

    @Column(name = "promotion_date")
    private LocalDate promotionDate;

    @Column(name = "salary_increment_amount")
    private Double salaryIncrementAmount;

    @Column(name = "promotion_reason")
    private String promotionReason;

    @Column(name = "approved_by_id")
    private Long approvedById;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;
}
