package com.company.payroll.department.model;

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
@Table(name = "department_facility_unit")
public class DepartmentFacilityUnit {
    @Id
    @Column(name = "department_fuid")
    private Long departmentFUId;

    @Column(name = "department_id", nullable = false)
    private Long departmentId;

    @Column(name = "facility_id", nullable = false)
    private Long facilityId;
}
