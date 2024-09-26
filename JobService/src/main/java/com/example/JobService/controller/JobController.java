package com.example.JobService.controller;

import com.example.JobService.model.JobPost;
import com.example.JobService.model.JobWithCompanyDTO;
import com.example.JobService.service.JobService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")  // please allow requests from this particular URL (react app)
// CrossOrigin simply means that if you are accessing the data from the outside world, it will be, by default, blocked.
// And if you want to allow it, you have to say CrossOrigin.
@Tag(name = "Job Posts Management", description = "APIs related to Job Post operations")
@SecurityRequirement(name = "JWTAuth")
@RequestMapping("/jobPosts")
public class JobController {
//    @Autowired
    private JobService service;

    public JobService getService() {
        return service;
    }

    @Autowired
    public void setService(JobService service) {
        this.service = service;
    }

    // get all job posts
    @GetMapping
    @Operation(summary = "Get all job posts", description = "Returns a list of all job posts")
    // @ResponseBody // we are returning JSON data not view // you don't need this if using @RestController
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of job posts"),
            @ApiResponse(responseCode = "204", description = "No job posts found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @CircuitBreaker(name = "companyBreaker", fallbackMethod = "companyBreakerFallback")
    public ResponseEntity<List<JobWithCompanyDTO>> getAllJobs(){
        return service.returnAllPosts();
    }

//    public List<String> companyBreakerFallback (Exception e) {
//        List<String> list = new ArrayList<>();
//        list.add("Dummy");
//        return list;
//    }
    // get one job post with postId
    @GetMapping("/{postId}")
    @Operation(summary = "Get one job post by ID", description = "Returns a single job post identified by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved job post"),
            @ApiResponse(responseCode = "404", description = "Job post not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<JobWithCompanyDTO> getOneJob(@PathVariable("postId") int postId){
        return service.returnOnePost(postId);
    }
    // can leave out @PathVariable("postId") if have the same name

    // post a job post
    @PostMapping
    @Operation(summary = "Create a new job post", description = "Creates a new job post and returns the created post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created job post"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<JobWithCompanyDTO> addOneJob(@RequestBody JobPost jobPost, HttpServletRequest request) {
        return service.addJobPost(jobPost, request);
    }

    // update a job post
    @PutMapping("/{postId}")
    @Operation(summary = "Update an existing job post", description = "Updates an existing job post and returns the updated post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated job post"),
            @ApiResponse(responseCode = "404", description = "Job post not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<JobPost> updateOneJob(@PathVariable int postId, @RequestBody JobPost jobPost) {
        return service.updateJobPost(postId, jobPost);
//        return service.returnOnePost(jobPost.getPostId());
    }

    // delete a post
    @DeleteMapping("/{postId}")
    @Operation(summary = "Delete a job post", description = "Deletes a job post identified by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted job post"),
            @ApiResponse(responseCode = "404", description = "Job post not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public String deleteOneJob(@PathVariable("postId") int postId) {
        service.deleteJobPost(postId);
        return "Deleted One Job Post";
    }

    // search data
    @GetMapping("/keyword/{keyword}")
    @Operation(summary = "Search job posts by keyword", description = "Searches for job posts that match the given keyword")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved job posts"),
            @ApiResponse(responseCode = "204", description = "No job posts found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<JobPost>> searchByKeyword(@PathVariable String keyword) {
        return service.search(keyword);
    }

//    // load initial data
//    @GetMapping("load")
//    // @Operation(summary = "Load initial data", description = "Loads initial data into the database")
//    public String load() {
//        service.load();
//        return "success";
//    }





}


