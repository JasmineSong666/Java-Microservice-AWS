package com.example.JobService.repo;

import com.example.JobService.model.JobPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface JobRepo extends JpaRepository<JobPost, Integer> {
        List<JobPost> findByPostProfileContainingOrPostDescContainingIgnoreCase(String postProfile, String postDesc);

}


//// get all job posts
//public List<JobPost> returnAllPosts() {
//    return jobs;
//}
//
//// get one job post by postId
//public JobPost returnOnePost(int postId) {
//    for (JobPost job : jobs) {
//        if (job.getPostId() == postId)
//            return job;
//    }
//
//    return null;
//}
//
//// add one job post
//public void addJobPost(JobPost jobPost) {
//    jobs.add(jobPost);
//}
//
////update a post
//public void updateJobPost(JobPost jobPost) {
//    for (JobPost job : jobs) {
//        if (job.getPostId() == jobPost.getPostId()) {
//            job.setPostProfile(jobPost.getPostProfile());
//            job.setPostDesc(jobPost.getPostDesc());
//            job.setReqExperience(jobPost.getReqExperience());
//            job.setPostTechStack(jobPost.getPostTechStack());
//        }
//    }
//}
//
//// delete a post
//public void deleteJobPost(int postId) {
//    jobs.removeIf(job -> job.getPostId() == postId);
//
////        for (JobPost job : jobs) {
////            if (job.getPostId() == postId)
////                jobs.remove(job);
////        }
//}