package com.company.payroll.compensation.dto;

import java.time.Instant;

public record CompensationStructureDetailDTO(
    Long compensationId,
    Instant createdAt,
    CompensationStructureDTO detail
) {

}
