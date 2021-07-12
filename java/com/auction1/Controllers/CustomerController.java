package com.auction1.Controllers;

import com.auction1.dao.CustomerDAO;
import com.auction1.models.Customer;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/auction")
public class CustomerController {


    private final CustomerDAO customerDAO;

    public CustomerController(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }





    @PostMapping("/newcustomer")
    public Customer createCustomer(@RequestParam String surname, @RequestParam String name,
                                   @RequestParam String patronymic,@RequestParam Date date,
                                   @RequestParam int ReferenceToLocation, @RequestParam String login,@RequestParam String password) throws SQLException {

        return customerDAO.createCustomer(surname,name, patronymic, date, ReferenceToLocation, login, password);
    }


    @GetMapping("/customer")
    public Customer getCustomer(@RequestParam int id) throws SQLException {
        return customerDAO.getCustomer(id);
    }

    @GetMapping("/customers")
    public List<Customer> getAllCustomer() throws SQLException {
        return customerDAO.getAllCustomers();
    }

}
