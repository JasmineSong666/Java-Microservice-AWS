package com.example.JobService.service;

import com.example.JobService.JobMapper;
import com.example.JobService.feign.JobInterface;
import com.example.JobService.model.CreateUserResponseModel;
import com.example.JobService.model.JobPost;
import com.example.JobService.model.JobWithCompanyDTO;
import com.example.JobService.model.User;
import com.example.JobService.repo.JobRepo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class JobService {

    private JobRepo jobRepo;

    private JobInterface jobInterface;

    private JobMapper jobMapper;

    public JobService(JobRepo jobRepo, JobInterface jobInterface) {
        this.jobRepo = jobRepo;
        this.jobInterface = jobInterface;
        this.jobMapper = jobMapper;
    }

    // Method to return all JobPosts
    public ResponseEntity<List<JobWithCompanyDTO>> returnAllPosts() {
        try {
            List<JobPost> jobPosts = jobRepo.findAll();
            if (jobPosts.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            List<JobWithCompanyDTO> jobWithCompanyDTO = new ArrayList<>();
            jobWithCompanyDTO = jobPosts.stream().map(this::convertToDto)
                    .collect(Collectors.toList());

            return new ResponseEntity<>(jobWithCompanyDTO, HttpStatus.OK);
        } catch (Exception e) {
//            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving job posts", e);
        }
    }

    private JobWithCompanyDTO convertToDto(JobPost jobpost){
        CreateUserResponseModel company = jobInterface.getCompanyByUserId(jobpost.getUserId()).getBody();
        return JobMapper.mapToJobWithCompanyDTO(jobpost, company);
    }


    // Method to return one post by ID
    public ResponseEntity<JobWithCompanyDTO> returnOnePost(int postId) {
        try {
            Optional<JobPost> jobPost = jobRepo.findById(postId); // it returns Optional  repo.findById(postId).orElse(new JobPost())

            if (jobPost.isPresent()) {
                JobWithCompanyDTO jobpost = convertToDto(jobPost.get());
                return new ResponseEntity<>(jobpost, HttpStatus.OK);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Job post not found");
            }
        } catch (ResponseStatusException e) {
            throw e; // Re-throw the exception to be caught by the GlobalExceptionHandler
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving job post", e);
        }
    }


    // Method to add a job post
    public ResponseEntity<JobWithCompanyDTO> addJobPost(JobPost jobPost, HttpServletRequest request) {
        try {
            // Retrieve the token from the request
            String token = request.getHeader("Authorization");

            // Use Feign client to get userId from user-service
            String userId = jobInterface.getUserIdFromToken(token).getBody();

            jobPost.setUserId(Integer.parseInt(userId));

            JobPost savedPost = jobRepo.save(jobPost);
            JobWithCompanyDTO jobpost = convertToDto(savedPost);
            return new ResponseEntity<>(jobpost, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error adding job post", e);
        }
    }

    // Method to update a job post
    public ResponseEntity<JobPost> updateJobPost(int postId, JobPost jobPost) {
        try {
            if (!jobRepo.existsById(postId)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Job post not found");
            }
            // Set the postId in the jobPost object
            jobPost.setPostId(postId);
            JobPost updatedJobPost = jobRepo.save(jobPost);
            return new ResponseEntity<>(updatedJobPost, HttpStatus.OK);
        } catch (ResponseStatusException e) {
            throw e; // Re-throw the exception to be caught by the GlobalExceptionHandler
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating job post", e);
        }
    }

    // Method to delete a job post
    public ResponseEntity<Void> deleteJobPost(int postId) {
       try {
           if (!jobRepo.existsById(postId)) {
               throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Job post not found");
           }
           jobRepo.deleteById(postId);
           return new ResponseEntity<>(HttpStatus.NO_CONTENT);
       } catch (ResponseStatusException e) {
           throw e; // Re-throw the exception to be caught by the GlobalExceptionHandler
       } catch (Exception e) {
           throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting job post", e);
       }
    }

    // Method to search posts with keyword
    public ResponseEntity<List<JobPost>> search(String keyword) {
        try {
            List<JobPost> jobPosts = jobRepo.findByPostProfileContainingOrPostDescContainingIgnoreCase(keyword, keyword);
            if (jobPosts.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(jobPosts, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error searching job posts", e);
        }
    }

//    // Method to load initial job posts
//    public ResponseEntity<Void> load() {
//        try {
//            // arrayList to store all JobPost objects
//            List<JobPost> jobs = new ArrayList<>(
//                    Arrays.asList(
//                            new JobPost(1, "Java Developer", "Must have good experience in core Java and advanced Java", 2,
//                                    List.of("Core Java", "J2EE", "Spring Boot", "Hibernate")),
//                            new JobPost(2, "Frontend Developer", "Experience in building responsive web applications using React",
//                                    3, List.of("HTML", "CSS", "JavaScript", "React")),
//                            new JobPost(3, "Data Scientist", "Strong background in machine learning and data analysis", 4,
//                                    List.of("Python", "Machine Learning", "Data Analysis")),
//                            new JobPost(4, "Network Engineer",
//                                    "Design and implement computer networks for efficient data communication", 5,
//                                    List.of("Networking", "Cisco", "Routing", "Switching")),
//                            new JobPost(5, "Mobile App Developer", "Experience in mobile app development for iOS and Android",
//                                    3, List.of("iOS Development", "Android Development", "Mobile App")),
//                            new JobPost(6, "DevOps Engineer", "Implement and manage continuous integration and delivery pipelines",
//                                    4, List.of("DevOps", "CI/CD", "Docker", "Kubernetes")),
//                            new JobPost(7, "UI/UX Designer", "Create engaging user experiences and intuitive user interfaces",
//                                    2, List.of("User Experience", "User Interface Design", "Prototyping")),
//                            new JobPost(8, "Cybersecurity Analyst", "Protect computer systems and networks from cyber threats",
//                                    4, List.of("Cybersecurity", "Network Security", "Incident Response")),
//                            new JobPost(9, "Full Stack Developer", "Experience in both front-end and back-end development",
//                                    5, List.of("JavaScript", "Node.js", "React", "Spring", "MongoDB")),
//                            new JobPost(10, "Cloud Architect", "Design and implement cloud infrastructure solutions", 6,
//                                    List.of("Cloud Computing", "AWS", "Azure", "Google Cloud")),
//                            new JobPost(11, "Software Tester", "Ensure software quality through testing and validation", 3,
//                                    List.of("Testing", "JUnit", "Selenium", "TestNG")),
//                            new JobPost(12, "React Native Developer",
//                                    "Develop cross-platform mobile applications using React Native", 2,
//                                    List.of("React Native", "JavaScript", "Mobile App Development")),
//                            new JobPost(13, "Business Analyst", "Analyze business processes and recommend improvements", 4,
//                                    List.of("Business Analysis", "Requirements Gathering", "Process Modeling")),
//                            new JobPost(14, "Embedded Systems Engineer",
//                                    "Design and develop embedded systems for hardware applications", 5,
//                                    List.of("Embedded Systems", "C/C++", "Microcontrollers", "RTOS")),
//                            new JobPost(15, "Content Writer",
//                                    "Create engaging and informative content for websites and publications", 2,
//                                    List.of("Content Writing", "Copywriting", "SEO", "Creative Writing")),
//                            new JobPost(16, "Business Intelligence Analyst",
//                                    "Utilize data to provide insights and support decision-making", 4,
//                                    List.of("Business Intelligence", "SQL", "Data Warehousing", "Tableau")),
//                            new JobPost(17, "UX Researcher", "Conduct user research to inform the design process", 3,
//                                    List.of("User Research", "Usability Testing", "Human-Computer Interaction")),
//                            new JobPost(18, "Backend Developer", "Build server-side logic and databases for web applications",
//                                    4, List.of("Java", "Spring", "Node.js", "MongoDB")),
//                            new JobPost(19, "Game Developer", "Create and optimize games for various platforms", 3,
//                                    List.of("Game Development", "Unity", "C#", "3D Modeling")),
//                            new JobPost(20, "IT Project Manager", "Lead and manage IT projects from initiation to completion",
//                                    6, List.of("Project Management", "Agile", "Scrum", "Risk Management"))
//                    )
//            );
//            jobRepo.saveAll(jobs);
//            return new ResponseEntity<>(HttpStatus.CREATED);
//        } catch (Exception e) {
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error loading job posts", e);
//        }
//    }


}
