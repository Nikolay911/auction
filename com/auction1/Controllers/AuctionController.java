package com.auction1.Controllers;


import com.auction1.dao.AuctionDAO;
import com.auction1.models.AuctionModel;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;

@RestController
public class AuctionController {

    private final AuctionDAO auctionDAO;

    public AuctionController(AuctionDAO auctionDAO) {
        this.auctionDAO = auctionDAO;
    }

    @PostMapping("/createAuction")
    public AuctionModel createAuction(@RequestParam int idCustomer, @RequestParam Timestamp completeTime,@RequestParam int idProduct){
        return auctionDAO.createAuction(idCustomer, completeTime, idProduct);
    }
}
