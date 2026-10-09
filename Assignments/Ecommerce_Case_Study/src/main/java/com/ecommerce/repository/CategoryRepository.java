package com.ecommerce.repository;

import com.ecommerce.mapper.CategoryMapper;
import com.ecommerce.model.Category;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryRepository {
    private final JdbcTemplate jdbcTemplate;
    private final CategoryMapper categoryMapper;

    public CategoryRepository(JdbcTemplate jdbcTemplate, CategoryMapper categoryMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.categoryMapper = categoryMapper;
    }

    public Category getCategory(String s) {
        String sql = "select * from category where name = ?";
        return jdbcTemplate.queryForObject(sql, categoryMapper, s);
    }
}
