package com.company.payroll.facility.dto;

import java.time.Instant;

public record CompanyFacilityDetailDTO(
        Long facilityId,
        Instant createdAt,
        CompanyFacilityDTO detail
) {
}
