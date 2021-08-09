package com.auction1.Controllers;

import com.auction1.dao.CustomerDAO;
import com.auction1.dao.LocationDAO;
import com.auction1.models.Customer;
import com.auction1.models.Location;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/auction")
public class CustomerController {

    private final CustomerDAO customerDAO;
    private final LocationDAO locationDAO;

    public CustomerController(CustomerDAO customerDAO, LocationDAO locationDAO) {
        this.customerDAO = customerDAO;
        this.locationDAO = locationDAO;
    }


    @PostMapping("/customer")
    public Customer createCustomer(@Nullable @RequestBody JsonNode customer) throws SQLException {
        return customerDAO.createCustomer(customer);
    }

    @GetMapping("/customer/{id}")
    public Customer getCustomer(@PathVariable int id) throws SQLException {
        return customerDAO.getCustomer(id);
    }

    @GetMapping("/customer")
    public List<Customer> getAllCustomer() throws SQLException {
        return customerDAO.getAllCustomers();
    }

    @GetMapping("/customer/{idCustomer}/locations")
    public List<Location> getCustomerInf(@PathVariable int idCustomer) throws SQLException {
        return locationDAO.getAllLocationToCustomer(idCustomer);
    }

}
