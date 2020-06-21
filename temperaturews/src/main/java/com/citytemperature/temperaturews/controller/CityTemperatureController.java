package com.citytemperature.temperaturews.controller;

import com.citytemperature.temperaturews.TemperaturewsApplication;
import com.citytemperature.temperaturews.exception.*;
import com.citytemperature.temperaturews.models.DetailLocation;
import com.citytemperature.temperaturews.models.ConsolidatedWeather;
import com.citytemperature.temperaturews.models.Location;
import com.citytemperature.temperaturews.models.Temperature;
import com.citytemperature.temperaturews.services.ItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.citytemperature.temperaturews.constant.MessagesConstant.*;

@RestController
public class CityTemperatureController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TemperaturewsApplication.class);

    @Autowired
    private ItemService itemService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/tempeture")
    public List<Temperature> viewLocation(@RequestParam("city") String city) throws IOException {
        List<Temperature> temperature = new ArrayList<Temperature>();
        Temperature tempCity = new Temperature();

        try {
            if (validatedCity(city)) {
                if(validateString(city)){
                    LOGGER.info(START_API_LOCATION + city);
                    List<Location> location = itemService.getLocation(city);

                    if (validateLocation(location)) {
                        LOGGER.info(QUERY_LOCATION_OK + city);

                        for (Location tempLoc: location){
                            tempCity.setNameCity(tempLoc.getTitle());
                            LOGGER.info(START_API_WEATHER + city);
                            DetailLocation detailLocation = itemService.getTemperature(tempLoc.getWoeid());

                            if (validateDetailLocation(detailLocation)) {
                                ConsolidatedWeather[] detailWeathers = detailLocation.getConsolidated_weather();
                                LOGGER.info(QUERY_WEATHER_OK + city);
                                ConsolidatedWeather weather = foundTodayWeather(detailWeathers);
                                tempCity.setTempC(weather.getThe_temp());
                                tempCity.setTempF(weather.getThe_temp());
                                temperature.add(tempCity);
                                tempCity = new Temperature();
                            } else {
                                LOGGER.info(NOT_FOUND_DETAIL + city);
                                throw new NotFoundException(NOT_FOUND_DETAIL + city);
                            }
                        }

                    } else {
                        LOGGER.info(NOT_FOUND_INFORMATION + city);
                        throw new NotFoundException(NOT_FOUND_INFORMATION + city);
                    }
                }else{
                    LOGGER.info(ERROR_TYPE_CITY);
                    throw new BadRequestException(ERROR_TYPE_CITY);
                }
            } else {
                LOGGER.info(ERROR_CITY_EMPTY);
                throw new BadRequestException(ERROR_CITY_EMPTY);
            }

        } catch (ArrayIndexOutOfBoundsException exception) {
            LOGGER.error(ERROR_MESSAGE + exception);
            throw new NotFoundException(ERROR_MESSAGE + exception);
        }
        return temperature;
    }

    public boolean validatedCity(String city){
        if(null != city && !"".equals(city)){
            return true;
        }else {
            return false;
        }
    }

    public boolean validateString(String city) {
        String expression = "^[a-zA-Z ]*$";
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(city);
        if(matcher.matches()){
            return true;
        }else{
            return false;
        }
    }

    public boolean validateLocation(List<Location> location){
        if (null != location && location.size()>0){
            return true;
        }else {
            return false;
        }
    }

    public boolean validateDetailLocation(DetailLocation detailLocation){
        if(null != detailLocation){
            if(null != detailLocation.getConsolidated_weather() && detailLocation.getConsolidated_weather().length>0){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    public ConsolidatedWeather foundTodayWeather(ConsolidatedWeather[] detailWeathers){
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        ConsolidatedWeather weather = new ConsolidatedWeather();

        for (int i = 0; i < detailWeathers.length; i++) {
            if (detailWeathers[i].getApplicable_date().equals(date.format(formatter))) {
                weather = detailWeathers[i];
                break;
            }
        }
        return weather;
    }
}

