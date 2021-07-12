package com.auction1.Controllers;


import com.auction1.dao.AuctionDAO;
import com.auction1.models.AuctionModel;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.sql.Timestamp;

@RestController
@RequestMapping("/auction")
public class AuctionController {

    private final AuctionDAO auctionDAO;

    public AuctionController(AuctionDAO auctionDAO) {
        this.auctionDAO = auctionDAO;
    }

    @PostMapping("/newauction")
    public AuctionModel createAuction(@RequestParam int idCustomer, @RequestParam Timestamp completeTime,@RequestParam int idProduct) throws SQLException {
        return auctionDAO.createAuction(idCustomer, completeTime, idProduct);
    }
}
