package com.ecommerce.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

@Component
public class VendorCountMapper implements RowMapper<Map<String, Integer>> {

    @Nullable
    @Override
    public Map<String, Integer> mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Map.of(
                rs.getString("name"),
                rs.getInt("product_count")
        );
    }
}
