// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.data.cosmostutorial;

import com.azure.spring.data.cosmos.core.query.CosmosPageRequest;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Iterator;

@SpringBootApplication
//public class SampleApplication implements CommandLineRunner {
public class SampleApplication {

    private final Logger logger = LoggerFactory.getLogger(SampleApplication.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReactiveUserRepository reactiveUserRepository;

    public static void main(String[] args) {
        SpringApplication.run(SampleApplication.class, args);
    }

    public void run0(String... var1) {

        final User testUser1 = new User("testId1", "testFirstName", "testLastName1");
        final User testUser2 = new User("testId2", "testFirstName", "testLastName2");
        final User testUser3 = new User("testId3", "testFirstName2", "testLastName3");

        logger.info("Using sync repository");

        // <Delete>

        userRepository.deleteAll();

        // </Delete>

        // <Create>

        logger.info("Saving user : {}", testUser1);
        userRepository.save(testUser1);

        // </Create>

        logger.info("Saving user : {}", testUser2);
        userRepository.save(testUser2);

        // <Read>        
        
        // to find by Id, please specify partition key value if collection is partitioned
        final User result = userRepository.findByIdAndLastName(testUser1.getId(), testUser1.getLastName());
        logger.info("Found user : {}", result);
        
        // </Read>        
        
        Iterator<User> usersIterator = userRepository.findByFirstName("testFirstName").iterator();

        logger.info("Users by firstName : testFirstName");
        while (usersIterator.hasNext()) {
            logger.info("user is : {}", usersIterator.next());
        }

        logger.info("Using reactive repository");

        // <Query>

        Flux<User> users = reactiveUserRepository.findByFirstName("testFirstName");
        users.map(u -> {
            logger.info("user is : {}", u);
            return u;
        }).subscribe();

        // </Query>

        //  Count users before saving
        Long count = reactiveUserRepository.count().block();
        logger.info("Count is : {}", count);

        PageRequest cosmosPageRequest = CosmosPageRequest.of(0, 100);
        Page<JsonNode> firstName = userRepository.findFirstNameById(testUser1.getId(), cosmosPageRequest);
        logger.info("Found firstName by id : {}", firstName.getContent().get(0));

        //  Save another user
        reactiveUserRepository.save(testUser3).block();

        //  Count again to verify user is saved and new count is 3
        Mono<Long> countMono = reactiveUserRepository.count();
        countMono.doOnNext(countValue -> {
            logger.info("Count is : {}", countValue);
        }).subscribe();
    }
}
