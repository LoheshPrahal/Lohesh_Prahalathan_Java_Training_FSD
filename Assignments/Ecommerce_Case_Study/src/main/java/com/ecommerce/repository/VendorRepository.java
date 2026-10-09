package com.ecommerce.repository;

import com.ecommerce.mapper.VendorCountMapper;
import com.ecommerce.mapper.VendorMapper;
import com.ecommerce.model.Vendor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class VendorRepository {
    private final JdbcTemplate jdbcTemplate;
    private final VendorMapper vendorMapper;
    private final VendorCountMapper vendorCountMapper;

    public VendorRepository(JdbcTemplate jdbcTemplate, VendorMapper vendorMapper, VendorCountMapper vendorCountMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.vendorMapper = vendorMapper;
        this.vendorCountMapper = vendorCountMapper;
    }

    public void insertVendor(Vendor vendor) {
        String sql = "insert into vendor values (?,?,?)";
        Object[] values = new Object[] {vendor.getId(), vendor.getName(), vendor.getEmail()};
        jdbcTemplate.update(sql, values);
    }

    public Vendor getVendor(String s) {
        String sql = "select * from vendor where name = ?";
        return jdbcTemplate.queryForObject(sql, vendorMapper, s);
    }

    public Map<String, Integer> countProductsByVendor() {
        String sql =
                """
                select v.name, count(p.id) as product_count
                from vendor v
                join product p on p.vendor_id = v.id
                group by v.id, v.name
                """;
                List<Map<String, Integer>> list = jdbcTemplate.query(sql, vendorCountMapper);
                Map<String, Integer>  map = new HashMap<>();
                for(Map<String, Integer> val : list){
                    map.putAll(val);
                }

                return map;

    }
}
