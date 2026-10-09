package com.ecommerce.service;

import com.ecommerce.dto.ProductDto;
import com.ecommerce.model.Product;
import com.ecommerce.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void insertProduct(Product product) {
        int productId = (int) (Math.random() * 100000);

        product.setId(productId);
        productRepository.insertProduct(product);
    }

    public ProductDto getProductById(int id) {
        return productRepository.getProductById(id);
    }


    public void updateStockQuantity(int id, int newStockQuantity) {
        productRepository.updateStockQuantity(id, newStockQuantity);
    }


}
