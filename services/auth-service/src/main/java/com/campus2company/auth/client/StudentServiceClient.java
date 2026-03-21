package com.campus2company.auth.client;

import com.campus2company.common.dto.request.CreateStudentRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "student-service", url = "http://student-service:8080")
public interface StudentServiceClient {

    @PostMapping("/students/createStudent")
    void createStudent(@RequestBody CreateStudentRequest dto, @RequestHeader("Authorization") String authorization);
}
