package com.ecommerce.repository;

import com.ecommerce.dto.ProductDto;
import com.ecommerce.mapper.ProductMapper;
import com.ecommerce.model.Category;
import com.ecommerce.model.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepository {

    private final JdbcTemplate jdbcTemplate;
    private final ProductMapper productMapper;

    public ProductRepository(JdbcTemplate jdbcTemplate, ProductMapper productMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.productMapper = productMapper;
    }

    public void insertProduct(Product product) {
        String sql = "insert into product values (?,?,?,?,?,?)";
        Object[] values = new Object[]{product.getId(), product.getName(), product.getPrice(), product.getStockQuantity(), product.getCategory().getId(), product.getVendor().getId()};
        jdbcTemplate.update(sql, values);
    }


    public ProductDto getProductById(int id) {
        String sql = """
                select p.id, p.name, p.price, p.stock_quantity, c.name as category_name, v.name as vendor_name
                from product p
                join category c on p.category_id = c.id
                join vendor v on p.vendor_id = v.id
                where p.id = ?
                """;
        return jdbcTemplate.queryForObject(sql, productMapper, id);
    }


    public void updateStockQuantity(int id, int newStockQuantity) {
        String sql = "update product set stock_quantity = ? where id = ?;";
        Object[] values = new Object[] {id, newStockQuantity};
        jdbcTemplate.update(sql, values);
    }
}
