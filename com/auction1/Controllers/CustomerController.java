package com.auction1.Controllers;

import com.auction1.dao.CustomerDAO;
import com.auction1.models.Customer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.util.List;

@RestController
public class CustomerController {


    private final CustomerDAO customerDAO;

    public CustomerController(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }





    @PostMapping("/createCustomer")
    public Customer createCustomer(@RequestParam String surname, @RequestParam String name,
                                   @RequestParam String patronymic,@RequestParam Date date,
                                   @RequestParam int ReferenceToLocation, @RequestParam String login,@RequestParam String password){

        return customerDAO.createCustomer(surname,name, patronymic, date, ReferenceToLocation, login, password);
    }


    @GetMapping("/getCustomer")
    public Customer getCustomer(@RequestParam int id){
        return customerDAO.getCustomer(id);
    }

    @GetMapping("/getAllCustomer")
    public List<Customer> getAllCustomer(){
        return customerDAO.getAllCustomers();
    }

}
