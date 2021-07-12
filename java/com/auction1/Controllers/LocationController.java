package com.auction1.Controllers;


import com.auction1.dao.LocationDAO;
import com.auction1.models.Location;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.sql.SQLException;

@RestController
@RequestMapping("/auction")
public class LocationController {

    private final LocationDAO locationDAO;

    public LocationController(LocationDAO locationDAO) {
        this.locationDAO = locationDAO;
    }

    @PostMapping("/newcustomerlocation")
    public boolean addLocationToCustomer(@RequestParam int idObjectCustomer, @RequestParam String city,
                                          @RequestParam String street, @RequestParam String home,
                                          @RequestParam int apartment, @RequestParam String postcode) throws SQLException {
        return locationDAO.addLocationToCustomer(idObjectCustomer, city, street, home, apartment, postcode);
    }
}
