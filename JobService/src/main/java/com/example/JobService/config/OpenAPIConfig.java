package com.example.JobService.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    // hide "load" path
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public-apis")
                .pathsToMatch("/jobPosts/**", "/jobPost/**", "/register", "/login") // Include only public user and product endpoints
                .build();
    }

    @Bean
    public OpenAPI myOpenAPI() {
        Contact contact = new Contact();
        contact.setEmail("jzsminesong@gmail.com");
        contact.setName("Song Zhijin");
        contact.setUrl("https://github.com/JasmineSong666");

        Info info = new Info()
                .title("Job Post API")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints to manage job posts.");

        return new OpenAPI().info(info);
    }

}
