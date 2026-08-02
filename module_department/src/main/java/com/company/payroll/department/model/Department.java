package com.company.payroll.department.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "department")
public class Department {
    @Id
    @Column(name = "department_id")
    private Long departmentId;

    @Column(name = "department_name", nullable = false)
    private String departmentName;

    @Column(name = "cost_center_code", unique = true)
    private String costCenterCode;

    @Column(name = "description")
    private String description;

    @Column(name = "parent_department_id")
    private Long parentDepartmentId;

    @Column(name = "location")
    private String location;

    @Column(name = "phone_extension_code")
    private String phoneExtensionCode;

    @Column(name = "department_email")
    private String departmentEmail;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;
}
