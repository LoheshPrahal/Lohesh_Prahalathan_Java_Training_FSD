package com.ecommerce.mapper;

import com.ecommerce.dto.ProductDto;
import com.ecommerce.model.Product;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;


@Component
public class ProductMapper implements RowMapper<ProductDto> {

    @Nullable
    @Override
    public ProductDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new ProductDto(
            rs.getInt("id"),
                rs.getString("name"),
                rs.getDouble("price"),
                rs.getInt("stock_quantity"),
                rs.getString("category_name"),
                rs.getString("vendor_name")
        );
    }
}
