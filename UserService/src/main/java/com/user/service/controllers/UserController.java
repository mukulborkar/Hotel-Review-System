package com.user.service.controllers;

import com.user.service.entities.User;
import com.user.service.services.UserService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User user1 = userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user1);
    }

    @GetMapping("/{userId}")
//    @CircuitBreaker(name = "ratingHotelBreaker", fallbackMethod = "ratingHotelFallback")
//    @Retry(name="ratingHotelService",fallbackMethod = "ratingHotelFallback")
    @RateLimiter(name = "userRateLimiter",fallbackMethod = "ratingHotelFallback")
    public ResponseEntity<User> getSingleUser(@PathVariable String userId) {
        User user = userService.getUser(userId);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    @CircuitBreaker(name="ratingHotelBreakerList", fallbackMethod = "ratingHotelListFallback")
    public ResponseEntity<List<User>> getUser() {
        List<User> allUser = userService.getAllUser();
        return ResponseEntity.ok(allUser);
    }

    // create ratingHotelFallback method

    static int retryCount =1;
    public ResponseEntity<User> ratingHotelFallback(String userId, Exception exc) {
//        logger.info("Fallback is executing because service is down" + exc.getMessage());
//        logger.info("Get Single User Handler :  UserController");
//        logger.info("Retry Count : {}",retryCount);
//        retryCount++;
        User dummy = User.builder()
                .email("dummy@gmail")
                .name("Dummy")
                .about("This is user is created because some service is down")
                .userId("123242").build();
        return ResponseEntity.ok(dummy);
    }
    public ResponseEntity<List<User>> ratingHotelListFallback(Exception exc){
        logger.info("Fallback is executing because service is down" + exc.getMessage());
        User dummy = User.builder()
                .email("dummy@gmail")
                .name("Dummy")
                .about("This is user is created because some service is down")
                .userId("123242").build();
        return ResponseEntity.ok(Arrays.asList(dummy));
    }
}
