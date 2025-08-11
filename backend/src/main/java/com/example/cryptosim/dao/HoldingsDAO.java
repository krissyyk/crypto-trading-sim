package com.example.cryptosim.dao;

import com.example.cryptosim.model.Holdings;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class HoldingsDAO {

    private final JdbcTemplate jdbcTemplate;

    public HoldingsDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Holdings> holdingsRowMapper = new RowMapper<>() {
        @Override
        public Holdings mapRow(ResultSet rs, int rowNum) throws SQLException {
            Holdings holdings = new Holdings();
            holdings.setId(rs.getInt("id"));
            holdings.setUserId(rs.getInt("user_id"));
            holdings.setCryptoName(rs.getString("crypto_name"));
            holdings.setQuantity(rs.getBigDecimal("quantity"));
            return holdings;
        }
    };

    public List<Holdings> findByUserId(int userId) {
        String sql = "SELECT * FROM holdings WHERE user_id = ?";
        return jdbcTemplate.query(sql, holdingsRowMapper, userId);
    }

    public Holdings findByUserIdAndCryptoName(int userId, String cryptoName) {
        String sql = "SELECT * FROM holdings WHERE user_id = ? AND crypto_name = ?";
        List<Holdings> results = jdbcTemplate.query(sql, holdingsRowMapper, userId, cryptoName);
        return results.isEmpty() ? null : results.get(0);
    }

    public int insert(Holdings holdings) {
        String sql = "INSERT INTO holdings (user_id, crypto_name, quantity) VALUES (?, ?, ?)";
        return jdbcTemplate.update(sql, holdings.getUserId(), holdings.getCryptoName(), holdings.getQuantity());
    }

    public int updateQuantity(int holdingsId, BigDecimal newQuantity) {
        String sql = "UPDATE holdings SET quantity = ? WHERE id = ?";
        return jdbcTemplate.update(sql, newQuantity, holdingsId);
    }

    public int deleteByUserIdAndCryptoName(int userId, String cryptoName) {
        String sql = "DELETE FROM holdings WHERE user_id = ? AND crypto_name = ?";
        return jdbcTemplate.update(sql, userId, cryptoName);
    }

    public int deleteAllByUserId(int userId) {
        String sql = "DELETE FROM holdings WHERE user_id = ?";
        return jdbcTemplate.update(sql, userId);
    }
}
