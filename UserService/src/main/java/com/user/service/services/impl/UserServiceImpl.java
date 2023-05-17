package com.user.service.services.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.user.service.entities.Hotel;
import com.user.service.entities.Rating;
import com.user.service.external.services.HotelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.user.service.entities.User;
import com.user.service.exception.ResourceNotFoundException;
import com.user.service.repositories.UserRepository;
import com.user.service.services.UserService;
import org.springframework.web.client.RestTemplate;

@Service
public class UserServiceImpl implements UserService {

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HotelService hotelService;

    @Override
    public User saveUser(User user) {
        String randomUserId = UUID.randomUUID().toString();
        user.setUserId(randomUserId);
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUser() {
        List<User> users = userRepository.findAll();
        List<Rating> ratingList = new ArrayList<>();
        List<Rating> ratings = new ArrayList<>();
        for (User user : users) {
            Rating[] ratingOfUser = restTemplate.getForObject("http://RATING-SERVICE/ratings/users/" + user.getUserId(), Rating[].class);
            ratings = Arrays.stream(ratingOfUser).toList();
            ratingList = ratings.stream().map(rating -> {
                // api call to hotel service
                // localhost:8082/hotels/c6cd779f-b7d4-4180-9742-e98631d36be5
//                ResponseEntity<Hotel> forEntity = restTemplate.getForEntity("http://HOTEL-SERVICE/hotels/" + rating.getHotelId(), Hotel.class);

                Hotel hotel = hotelService.getHotel(rating.getHotelId()).getBody();
//            logger.info("response status code : ", forEntity.getStatusCode());
                // set the hotel to rating
                rating.setHotel(hotel);
                // return rating
                return rating;
            }).collect(Collectors.toList());
            user.setRatings(ratingList);
        }
//        logger.info("{}",ratingsOfUser);
        return users;
    }

    @Override
    public User getUser(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with given id not found on server !! : " + id));

        // fetch rating from Rating Service
        //		http://localhost:8083/ratings/users/{userId}

        Rating[] ratingOfUser = restTemplate.getForObject("http://RATING-SERVICE/ratings/users/" + user.getUserId(), Rating[].class);
        List<Rating> ratings = Arrays.stream(ratingOfUser).toList();
//        user.setRatings(ratingOfUser);

        List<Rating> ratingList = ratings.stream().map(rating -> {
            // api call to hotel service
            // localhost:8082/hotels/c6cd779f-b7d4-4180-9742-e98631d36be5
//            ResponseEntity<Hotel> forEntity = restTemplate.getForEntity("http://HOTEL-SERVICE/hotels/" + rating.getHotelId(), Hotel.class);
            Hotel hotel = hotelService.getHotel(rating.getHotelId()).getBody();

//            logger.info("response status code : ", forEntity.getStatusCode());

            // set the hotel to rating
            rating.setHotel(hotel);

            // return rating
            return rating;
        }).collect(Collectors.toList());

        user.setRatings(ratingList);

        return user;
    }

    @Override
    public void deletUser(String id) {
        userRepository.deleteById(id);
    }

    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }

}
