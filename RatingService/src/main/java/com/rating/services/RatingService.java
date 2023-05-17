package com.rating.services;

import com.rating.entities.Rating;

import java.util.List;

public interface RatingService {

    Rating createRating(Rating rating);

    List<Rating> getAll();

    List<Rating> getAllByUserId(String userId);

    List<Rating> getAllByHotelId(String hotelId);
}
