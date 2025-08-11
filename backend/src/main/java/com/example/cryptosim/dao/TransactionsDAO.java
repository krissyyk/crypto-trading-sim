package com.example.cryptosim.dao;

import com.example.cryptosim.model.Transactions;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class TransactionsDAO {

    private final JdbcTemplate jdbcTemplate;

    public TransactionsDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Transactions> transactionsRowMapper = new RowMapper<>() {
        @Override
        public Transactions mapRow(ResultSet rs, int rowNum) throws SQLException {
            Transactions transaction = new Transactions();
            transaction.setId(rs.getInt("id"));
            transaction.setUserId(rs.getInt("user_id"));
            transaction.setCryptoName(rs.getString("crypto_name"));
            transaction.setTransactionType(rs.getString("transaction_type"));
            transaction.setQuantity(rs.getBigDecimal("quantity"));
            transaction.setPrice(rs.getBigDecimal("price"));
            transaction.setTotalAmount(rs.getBigDecimal("total_amount"));
            transaction.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
            return transaction;
        }
    };

    public List<Transactions> findByUserId(int userId) {
        String sql = "SELECT * FROM transactions WHERE user_id = ? ORDER BY timestamp DESC";
        return jdbcTemplate.query(sql, transactionsRowMapper, userId);
    }

    public int insert(Transactions transaction) {
        String sql = "INSERT INTO transactions (user_id, crypto_name, transaction_type, quantity, price, total_amount) VALUES (?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                transaction.getUserId(),
                transaction.getCryptoName(),
                transaction.getTransactionType(),
                transaction.getQuantity(),
                transaction.getPrice(),
                transaction.getTotalAmount());
    }

    public int deleteAllByUserId(int userId) {
        String sql = "DELETE FROM transactions WHERE user_id = ?";
        return jdbcTemplate.update(sql, userId);
    }
}
