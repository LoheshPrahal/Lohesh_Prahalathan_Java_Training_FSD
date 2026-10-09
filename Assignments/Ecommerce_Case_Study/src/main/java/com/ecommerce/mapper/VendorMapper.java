package com.ecommerce.mapper;

import com.ecommerce.enums.VendorName;
import com.ecommerce.model.Vendor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class VendorMapper implements RowMapper<Vendor> {

    @Nullable
    @Override
    public Vendor mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Vendor(
                rs.getInt("id"),
                VendorName.valueOf(rs.getString("name")),
                rs.getString("email")
        );
    }
}
