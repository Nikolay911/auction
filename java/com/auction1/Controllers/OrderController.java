package com.auction1.Controllers;


import com.auction1.dao.OrderDAO;
import com.auction1.models.Order;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
@RequestMapping("/auction")
public class OrderController {

    private final OrderDAO orderDAO;

    public OrderController(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }

    @PostMapping("/bid")
    public String bid(@RequestParam int idCustomer, @RequestParam int idAuction, @RequestParam double customerPrice) throws SQLException {
        return orderDAO.bid(idCustomer, idAuction, customerPrice);
    }

}
