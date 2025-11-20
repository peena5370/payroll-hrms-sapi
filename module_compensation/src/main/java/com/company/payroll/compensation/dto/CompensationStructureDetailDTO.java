package com.company.payroll.compensation.dto;

import java.time.LocalDateTime;

public record CompensationStructureDetailDTO(
    Long compensationId,
    LocalDateTime createdAt,
    CompensationStructureDTO detail
) {

}
