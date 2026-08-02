package com.company.payroll.employee.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "employee_emergency_contact")
public class EmployeeEmergencyContact {
    @Id
    @Column(name = "contact_id")
    private Long contactId;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "contact_name", nullable = false)
    private String contactName;

    @Column(name = "relationship", nullable = false)
    private String relationship;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "email")
    private String email;
}
