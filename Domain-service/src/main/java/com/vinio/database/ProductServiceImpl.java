package com.vinio.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Создание нового продукта
    public ProductEntity createProduct(ProductEntity product) {
        return productRepository.save(product);
    }

    // Получение продукта по ID
    public Optional<ProductEntity> getProductById(int id) {
        System.out.println("идём в базу: " + id);
        Optional<ProductEntity> product = productRepository.findById(id);
        System.out.println(product);
        return product;
    }

    // Получение всех продуктов
    public List<ProductEntity> getAllProducts() {
        return productRepository.findAll();
    }

    // Обновление существующего продукта
    public ProductEntity updateProduct(int id, ProductEntity updatedProduct) {
        return productRepository.findById(id).map(product -> {
            product.setName(updatedProduct.getName());
            product.setPrise(updatedProduct.getPrise());
            product.setCategory(updatedProduct.getCategory());
            product.setCount(updatedProduct.getCount());
            return productRepository.save(product);
        }).orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    // Удаление продукта по ID
    public void deleteProduct(int id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        } else {
            throw new RuntimeException("Product not found with id: " + id);
        }
    }
}
