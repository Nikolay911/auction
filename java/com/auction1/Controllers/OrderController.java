package com.auction1.Controllers;


import com.auction1.dao.OrderDAO;
import com.auction1.models.Order;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/auction")
public class OrderController {

    private final OrderDAO orderDAO;

    public OrderController(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }


    @PostMapping("/bid/{idAuction}/{idCustomer}")
    public String bid(@PathVariable int idCustomer,
                      @PathVariable int idAuction,
                      @Nullable @RequestBody JsonNode customerPrice) throws SQLException {
        return orderDAO.bid(idCustomer, idAuction, customerPrice);
    }

    @GetMapping("bid/{orderId}")
    public List<Order> getOrder(@PathVariable int orderId) throws SQLException {
        return orderDAO.getAllBid(orderId);
    }

}
