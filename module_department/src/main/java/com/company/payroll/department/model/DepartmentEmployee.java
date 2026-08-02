package com.company.payroll.department.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "department_employee")
public class DepartmentEmployee {
    @Id
    @Column(name = "department_eid")
    private Long departmentEid;

    @Column(name = "department_id", nullable = false)
    private Long departmentId;

    @Column(name = "department_fuid", nullable = false)
    private Long departmentFUId;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "is_primary")
    private Boolean primary;

    @Column(name = "is_manager")
    private Boolean manager;

    @Column(name = "joined_at", nullable = false)
    private LocalDate joinedAt;

    @Column(name = "leaved_at")
    private LocalDate leavedAt;
}
