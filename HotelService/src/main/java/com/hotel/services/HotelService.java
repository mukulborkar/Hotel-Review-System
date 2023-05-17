package com.hotel.services;

import com.hotel.entities.Hotel;

import java.util.List;

public interface HotelService {
    Hotel createHotel(Hotel hotel);

    List<Hotel> getAll();

    Hotel get(String id);
}
