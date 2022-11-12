package com.azure.spring.data.cosmostutorial;

import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;

@RestController
public class SampleController {

    private final Logger logger = LoggerFactory.getLogger(SampleController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReactiveUserRepository reactiveUserRepository;
        
    @GetMapping("/api/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        String msg = "Hello " + name;
        return msg;
    }

    @GetMapping("/api/userbyidandlastname") 
    public User getUserByIdAndLstname(@RequestParam(value = "id") String id, @RequestParam(value = "lastname") String lname) {
        User user = userRepository.findByIdAndLastName(id, lname);
        return user;
    }

    @GetMapping("/api/usersbyfirstname") 
    public ResponseEntity<?> getUsersByFirstname(@RequestParam(value = "firstname") String fName) {
        Iterator<User> usersIterator = userRepository.findByFirstName(fName).iterator();

        while (usersIterator.hasNext()) {
            logger.info("user is {}", usersIterator.next());
        }

        return new ResponseEntity(usersIterator, HttpStatus.OK);
    }

    @PostMapping("/api/user") 
    public void addUser(@RequestBody User user) {
        logger.info(user.toString()); 
        userRepository.save(user);
    }


    @GetMapping("/api/react/usersbyfirstname")
    public ResponseEntity<?> getUserByFirstnameAsync(@RequestParam(value = "firstname") String fName) {
        Flux<User> users = reactiveUserRepository.findByFirstName(fName);
        users.map(u -> {
            logger.info("user is {} ",u);
            return u;
        }).subscribe();

        return new ResponseEntity(users, HttpStatus.OK);
    }

    @PostMapping("/api/react/user")
    public void addUserAsync(@RequestBody User user) {
        reactiveUserRepository.save(user).block();
    }

}