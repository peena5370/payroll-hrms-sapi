package com.company.payroll.user.service.impl;

import com.company.payroll.user.dto.JwkUriResponse;
import com.company.payroll.user.service.PayrollCoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Slf4j
@Service
public class PayrollCoreServiceImpl implements PayrollCoreService {
    private static final String CLASS_NAME = "[PayrollCoreServiceImpl]";
    private final WebClient client;
    private static final String JWK_URI_URL = "/core/json/jwk_uri";

    public PayrollCoreServiceImpl(@Qualifier("payrollCoreClientBuilder") WebClient client) {
        this.client = client;
    }

    public JwkUriResponse getJwkUri() {
        final String functionName = "getJwkUri";
        JwkUriResponse response = null;
        try {
            response = client.post()
                    .uri(JWK_URI_URL)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, ClientResponse::createException
                    )
                    .bodyToMono(JwkUriResponse.class)
                    .block();

            if (response == null) {
                throw new IllegalStateException("Received null response from JWK URI endpoint.");
            }

            log.info("{} {} successfully fetched JWK URI.", CLASS_NAME, functionName);
        } catch (WebClientResponseException e) {
            log.error("{} {} Error fetching JWK URI. Status={}, Body={}", CLASS_NAME, functionName, e.getStatusCode(), e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("{} {} An unexpected error occurred={}", CLASS_NAME, functionName, e.getMessage());
        }

        return response;
    }
}
