package com.auction1.Controllers;


import com.auction1.dao.LocationDAO;
import com.auction1.models.Location;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/auction")
public class LocationController {

    private final LocationDAO locationDAO;

    public LocationController(LocationDAO locationDAO) {
        this.locationDAO = locationDAO;
    }


    @PostMapping("/customerlocation/{idObjectCustomer}")
    public boolean addLocationToCustomer(@PathVariable int idObjectCustomer,
                                         @Nullable @RequestBody JsonNode body) throws SQLException {
        return locationDAO.addLocationToCustomer(idObjectCustomer, body);
    }

    @GetMapping("/customerlocations/{idCustomer}")
    public List<Location> customerLocations(@PathVariable int idCustomer) throws SQLException {
        return locationDAO.getAllLocationToCustomer(idCustomer);
    }
}
