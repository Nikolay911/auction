package com.auction1.Controllers;


import com.auction1.dao.ProductDAO;
import com.auction1.models.Product;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/auction")
public class ProductController {

    private ProductDAO productDAO;


    public ProductController(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }


    @PostMapping("/product/{idCustomer}")
    public Product createProduct(@PathVariable int idCustomer,
                                 @Nullable @RequestBody JsonNode body) throws SQLException {
        return productDAO.createProduct(idCustomer, body);
    }

    @GetMapping("/product/{idProduct}")
    public Product getProduct(@PathVariable int idProduct) throws SQLException {
        return productDAO.getProduct(idProduct);
    }

}
