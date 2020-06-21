package com.citytemperature.temperaturews.services;

import com.citytemperature.temperaturews.TemperaturewsApplication;
import com.citytemperature.temperaturews.exception.*;
import com.citytemperature.temperaturews.models.DetailLocation;
import com.citytemperature.temperaturews.models.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static com.citytemperature.temperaturews.constant.MessagesConstant.*;

@Service
public class ItemServiceImpl implements ItemService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TemperaturewsApplication.class);

    @Value("${url.search.loccation}")
    private String urlLocation;

    @Value("${url.search.weather}")
    private String urlWeather;

    @Autowired
    private RestTemplate clientRest;

    @Override
    public List<Location> getLocation(String city) {
        Map<String, String> pathVariable = new HashMap<String, String>();
        pathVariable.put("city", city);
        List<Location> location = new ArrayList<Location>();

        try {
              location = Arrays.asList(clientRest.getForObject(urlLocation, Location[].class, pathVariable));

        } catch (HttpStatusCodeException ex) {
            if(HttpStatus.NOT_FOUND == ex.getStatusCode()){
                LOGGER.error(API_LOCATION_NOT_FOUND + ex.getMessage());
                throw new NotFoundException(API_LOCATION_NOT_FOUND);
            }
            if(HttpStatus.BAD_REQUEST == ex.getStatusCode()){
                LOGGER.error(ERROR_BAD_REQUEST + ex.getMessage());
                throw new BadRequestException(ERROR_BAD_REQUEST);
            }
            if(HttpStatus.INTERNAL_SERVER_ERROR == ex.getStatusCode()){
                LOGGER.error(ERROR_MESSAGE + ex.getMessage());
                throw new InternalErrorException(ERROR_MESSAGE);
            }
        }

        return location;
    }

    @Override
    public DetailLocation getTemperature(int woeid) {
        Map<String, String> pathVariable = new HashMap<String, String>();
        pathVariable.put("woeid", String.valueOf(woeid));
        DetailLocation detailLocation = new DetailLocation();

        try {
            detailLocation = clientRest.getForObject(urlWeather, DetailLocation.class, pathVariable);
        } catch (HttpStatusCodeException ex) {
            LOGGER.error(ERROR_MESSAGE+ "Code: "+ ex.getStatusCode().toString()+ "Message: " + ex.getMessage());

        }finally {
            return detailLocation;
        }

    }

}
