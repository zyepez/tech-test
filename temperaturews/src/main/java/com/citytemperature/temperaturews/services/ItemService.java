package com.citytemperature.temperaturews.services;

import com.citytemperature.temperaturews.models.DetailLocation;
import com.citytemperature.temperaturews.models.Location;

import java.io.IOException;
import java.util.List;

public interface ItemService {
    List<Location> getLocation(String city) throws IOException;
    DetailLocation getTemperature(int woeid);
}
