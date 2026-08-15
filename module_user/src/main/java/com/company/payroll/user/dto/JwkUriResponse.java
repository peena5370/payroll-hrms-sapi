package com.company.payroll.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record JwkUriResponse(@JsonProperty("status_code")Integer statusCode, String message, JwkUriData data) {
}
