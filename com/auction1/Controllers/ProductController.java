package com.auction1.Controllers;


import com.auction1.dao.ProductDAO;
import com.auction1.models.Product;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProductController {

    private ProductDAO productDAO;

    public ProductController(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @PostMapping("/createProduct")
    public Product createProduct(@RequestParam int customerId, @RequestParam String foto, @RequestParam String productDescription, @RequestParam Number startPrice){
        return productDAO.createProduct(customerId, foto, productDescription, startPrice);
    }

    @GetMapping("/getProduct")
    public Product getProduct(@RequestParam int id){
        return productDAO.getProduct(id);
    }

}
