package com.example.JobService;

import com.example.JobService.model.CreateUserResponseModel;
import com.example.JobService.model.JobPost;
import com.example.JobService.model.JobWithCompanyDTO;
import com.example.JobService.model.User;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

//@Component
public class JobMapper {
    public static JobWithCompanyDTO mapToJobWithCompanyDTO (JobPost jobPost, CreateUserResponseModel company) {
        JobWithCompanyDTO  jobWithCompanyDTO = new JobWithCompanyDTO();
        jobWithCompanyDTO.setPostId(jobPost.getPostId());
        jobWithCompanyDTO.setPostDesc(jobPost.getPostDesc());
        jobWithCompanyDTO.setPostProfile(jobPost.getPostProfile());
        jobWithCompanyDTO.setReqExperience(jobPost.getReqExperience());
        jobWithCompanyDTO.setPostTechStack(jobPost.getPostTechStack());
        jobWithCompanyDTO.setCompany(company);
        return jobWithCompanyDTO;
    }
}
