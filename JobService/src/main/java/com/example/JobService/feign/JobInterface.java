package com.example.JobService.feign;

import com.example.JobService.model.CreateUserResponseModel;
import com.example.JobService.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-service",
        url = "${user-service.url}")
public interface JobInterface {
    @GetMapping("user/{userId}")
    public ResponseEntity<CreateUserResponseModel> getCompanyByUserId
            (@PathVariable int userId);

    @GetMapping("user/getUserIdFromToken")
    public ResponseEntity<String> getUserIdFromToken (@RequestHeader("Authorization") String token);
}
