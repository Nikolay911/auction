package com.auction1.Controllers;


import com.auction1.dao.AuctionDAO;
import com.auction1.dao.OrderDAO;
import com.auction1.models.AuctionModel;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/auction")
public class AuctionController {

    private final AuctionDAO auctionDAO;
    private final OrderDAO orderDAO;


    public AuctionController(AuctionDAO auctionDAO, OrderDAO orderDAO) {
        this.auctionDAO = auctionDAO;
        this.orderDAO = orderDAO;
    }


    @PostMapping("/auction/{idCustomer}/{idProduct}")
    public AuctionModel createAuction(@PathVariable int idCustomer,
                                      @PathVariable int idProduct,
                                      @Nullable @RequestBody JsonNode body) throws Exception {
        return auctionDAO.createAuction(idCustomer, idProduct, body);
    }

    @GetMapping("/{auctionId}")
    public AuctionModel getAuction(@PathVariable int auctionId) throws SQLException {
        return auctionDAO.getAuction(auctionId);
    }

    @GetMapping("/all")
    public List<AuctionModel> getAllAuction() throws SQLException {
        return auctionDAO.getAllAuctions();
    }
}
