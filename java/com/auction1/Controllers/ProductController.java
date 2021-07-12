package com.auction1.Controllers;


import com.auction1.dao.ProductDAO;
import com.auction1.models.Product;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.sql.SQLException;

@RestController
@RequestMapping("/auction")
public class ProductController {

    private ProductDAO productDAO;

    public ProductController(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @PostMapping("/newproduct")
    public Product createProduct(@RequestParam int customerId, @RequestParam String foto, @RequestParam String productDescription, @RequestParam Number startPrice) throws SQLException, FileNotFoundException {
        return productDAO.createProduct(customerId, foto, productDescription, startPrice);
    }

    @GetMapping("/product")
    public Product getProduct(@RequestParam int id) throws SQLException {
        return productDAO.getProduct(id);
    }

}
