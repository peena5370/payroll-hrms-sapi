package com.company.payroll.resignation.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "employee_resignation")
public class EmployeeResignation {
    @Id
    @Column(name = "resignation_id")
    private Long resignationId;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "resignation_date", nullable = false)
    private LocalDate resignationDate;

    @Column(name = "last_working_day")
    private LocalDate lastWorkingDay;

    @Column(name = "resignation_reason")
    private String resignationReason;

    @Column(name = "notice_period_day", nullable = false)
    private Integer noticePeriodDays;

    @Column(name = "exit_interview_conducted", nullable = false)
    private Boolean exitInterviewConducted;

    @Column(name = "exit_interview_note")
    private String exitInterviewNote;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "approved_by_id")
    private Long approvedById;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;
}
