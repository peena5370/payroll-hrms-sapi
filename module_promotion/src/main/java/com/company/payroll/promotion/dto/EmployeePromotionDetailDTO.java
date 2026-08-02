package com.company.payroll.promotion.dto;

import java.time.Instant;

public record EmployeePromotionDetailDTO(
        Long promotionId,
        Instant createdAt,
        EmployeePromotionDTO detail
) {
}
