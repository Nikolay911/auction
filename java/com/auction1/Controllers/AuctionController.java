package com.auction1.Controllers;


import com.auction1.dao.AuctionDAO;
import com.auction1.models.AuctionModel;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import java.sql.SQLException;

@RestController
@RequestMapping("/auction")
public class AuctionController {

    private final AuctionDAO auctionDAO;


    public AuctionController(AuctionDAO auctionDAO) {
        this.auctionDAO = auctionDAO;
    }


    @PostMapping("/auction/{idCustomer}/{idProduct}")
    public AuctionModel createAuction(@PathVariable int idCustomer,
                                      @PathVariable int idProduct,
                                      @Nullable @RequestBody JsonNode body) throws SQLException {
        return auctionDAO.createAuction(idCustomer, idProduct, body);
    }
}
