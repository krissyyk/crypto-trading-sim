package com.example.cryptosim.dao;

import com.example.cryptosim.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UserDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String SELECT_ALL = "SELECT * FROM users";
    private static final String SELECT_BY_ID = "SELECT * FROM users WHERE id = ?";
    private static final String SELECT_BY_USERNAME = "SELECT * FROM users WHERE username = ?";
    private static final String INSERT_USER = "INSERT INTO users (username, balance) VALUES (?, ?)";
    private static final String UPDATE_BALANCE = "UPDATE users SET balance = ? WHERE id = ?";

    public List<User> findAll() {
        return jdbcTemplate.query(SELECT_ALL, new UserRowMapper());
    }

    public User findById(int id) {
        return jdbcTemplate.queryForObject(SELECT_BY_ID, new Object[]{id}, new UserRowMapper());
    }

    public User findByUsername(String username) {
        return jdbcTemplate.queryForObject(SELECT_BY_USERNAME, new Object[]{username}, new UserRowMapper());
    }

    public int insert(String username, BigDecimal initialBalance) {
        return jdbcTemplate.update(INSERT_USER, username, initialBalance);
    }

    public int updateBalance(int id, BigDecimal newBalance) {
        return jdbcTemplate.update(UPDATE_BALANCE, newBalance, id);
    }

    private static final class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setUsername(rs.getString("username"));
            user.setBalance(rs.getBigDecimal("balance"));
            return user;
        }
    }
}
