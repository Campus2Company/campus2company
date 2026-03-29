package com.campus2company.auth.client;

import com.campus2company.common.dto.request.CreateEmployerRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "employer-service", url = "http://employer-service:8080")
public interface EmployerServiceClient {

    @PostMapping("/employers/createEmployer")
    void createEmployer(@RequestBody CreateEmployerRequest dto, @RequestHeader("Authorization") String authorization);
}
