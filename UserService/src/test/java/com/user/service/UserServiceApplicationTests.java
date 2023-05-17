package com.user.service;

import com.user.service.entities.Rating;
import com.user.service.external.services.RatingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserServiceApplicationTests {

    @Test
    void contextLoads() {
    }

//    @Autowired
//    private RatingService ratingService;

//    @Test
//    void createRating() {
//
//        Rating rating = Rating.builder().rating(10).userId("").hotelId("").feedback("This is created using Feign").build();
//        ratingService.createRating(rating);
//        System.out.println("new rating created");
//    }
}
