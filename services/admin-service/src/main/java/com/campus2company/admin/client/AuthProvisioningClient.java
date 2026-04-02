package com.campus2company.admin.client;

import com.campus2company.admin.dto.AuthUserResponse;
import com.campus2company.admin.dto.ProvisionAccountRequest;
import com.campus2company.admin.exception.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthProvisioningClient {

    private final RestClient authRestClient;

    public AuthUserResponse provisionAccount(ProvisionAccountRequest body, String authorizationHeader) {
        try {
            return authRestClient.post()
                    .uri("/admin/accounts")
                    .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .body(AuthUserResponse.class);
        } catch (RestClientResponseException ex) {
            String responseBody = ex.getResponseBodyAsString();
            log.warn("Auth provisioning failed: {} — {}", ex.getStatusCode(), responseBody);
            throw new ApiException(
                    "Auth service rejected the request (" + ex.getStatusCode().value() + ")",
                    HttpStatus.BAD_GATEWAY);
        }
    }
}
