package com.ecommerce.dto;

public record ProductDto(
    int productId,
    String name,
    double price,
    int stockQuantity,
    String categoryName,
    String vendorName
) {
}
