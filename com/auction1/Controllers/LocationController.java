package com.auction1.Controllers;


import com.auction1.dao.LocationDAO;
import com.auction1.models.Location;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;

@RestController
public class LocationController {

    private final LocationDAO locationDAO;

    public LocationController(LocationDAO locationDAO) {
        this.locationDAO = locationDAO;
    }

    @PutMapping("/createCustomerLocation")
    public boolean addLocationToCustomer(@RequestParam int idObjectCustomer, @RequestParam String city,
                                          @RequestParam String street, @RequestParam String home,
                                          @RequestParam int apartment, @RequestParam String postcode){
        return locationDAO.addLocationToCustomer(idObjectCustomer, city, street, home, apartment, postcode);
    }
}
