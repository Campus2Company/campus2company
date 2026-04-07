package com.campus2company.admin.client;

import com.campus2company.admin.dto.AuthUserResponse;
import com.campus2company.admin.dto.ProvisionUniversityAdminAuthRequest;
import com.campus2company.admin.exception.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthPlatformClient {

    private final RestClient authRestClient;

    public AuthUserResponse provisionUniversityAdmin(ProvisionUniversityAdminAuthRequest body, String authorizationHeader) {
        return postJson("/admin/university-admins", body, authorizationHeader, AuthUserResponse.class);
    }

    public List<AuthUserResponse> listPendingEmployers(String authorizationHeader) {
        try {
            return authRestClient.get()
                    .uri("/admin/employers/pending")
                    .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<AuthUserResponse>>() {});
        } catch (RestClientResponseException ex) {
            log.warn("Auth list pending employers failed: {} — {}", ex.getStatusCode(), ex.getResponseBodyAsString());
            throw new ApiException(
                    "Auth service rejected the request (" + ex.getStatusCode().value() + ")",
                    HttpStatus.BAD_GATEWAY);
        }
    }

    public AuthUserResponse approveEmployer(UUID userId, String authorizationHeader) {
        return putEmpty("/admin/employers/" + userId + "/approve", authorizationHeader, AuthUserResponse.class);
    }

    public AuthUserResponse rejectEmployer(UUID userId, String authorizationHeader) {
        return putEmpty("/admin/employers/" + userId + "/reject", authorizationHeader, AuthUserResponse.class);
    }

    private <T> T postJson(String uri, Object body, String authorizationHeader, Class<T> responseType) {
        try {
            return authRestClient.post()
                    .uri(uri)
                    .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .body(responseType);
        } catch (RestClientResponseException ex) {
            String responseBody = ex.getResponseBodyAsString();
            log.warn("Auth request failed: {} — {}", ex.getStatusCode(), responseBody);
            throw new ApiException(
                    "Auth service rejected the request (" + ex.getStatusCode().value() + ")",
                    HttpStatus.BAD_GATEWAY);
        }
    }

    private <T> T putEmpty(String uri, String authorizationHeader, Class<T> responseType) {
        try {
            return authRestClient.put()
                    .uri(uri)
                    .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                    .retrieve()
                    .body(responseType);
        } catch (RestClientResponseException ex) {
            log.warn("Auth request failed: {} — {}", ex.getStatusCode(), ex.getResponseBodyAsString());
            throw new ApiException(
                    "Auth service rejected the request (" + ex.getStatusCode().value() + ")",
                    HttpStatus.BAD_GATEWAY);
        }
    }
}
