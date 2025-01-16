package org.vinio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInit implements CommandLineRunner {

    private final ProductService productService;

    @Autowired
    public DataInit(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public void run(String... args) {
        productService.createProduct(new ProductEntity("Laptop", 1200.50, "Electronics", 15));
        productService.createProduct(new ProductEntity("Smartphone", 800.00, "Electronics", 30));
        productService.createProduct(new ProductEntity("Headphones", 150.25, "Accessories", 50));
        productService.createProduct(new ProductEntity("Backpack", 70.99, "Bags", 40));
        productService.createProduct(new ProductEntity("Smartwatch", 300.00, "Electronics", 25));
        productService.createProduct(new ProductEntity("Keyboard", 100.75, "Accessories", 60));
        productService.createProduct(new ProductEntity("Mouse", 50.00, "Accessories", 70));
        productService.createProduct(new ProductEntity("Monitor", 250.99, "Electronics", 20));
        productService.createProduct(new ProductEntity("Desk Lamp", 45.89, "Furniture", 35));
        productService.createProduct(new ProductEntity("Notebook", 5.50, "Stationery", 100));

        System.out.println("Data initialization complete: 10 products added.");
    }
}
