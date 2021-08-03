package com.ecc.setubot.dao.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class SetuRankTotalDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public void increase(Long Id, int count) {
        String sql = "UPDATE setu_rank_total SET get_setu_count = get_setu_count + ? WHERE user_id = ?";
        jdbcTemplate.update(sql, count, Id);
    }

    public Integer getSetuCount(Long id) {
        String sql = "SELECT get_setu_count FROM setu_rank_total WHERE user_id = ?";
        List<Integer> count = jdbcTemplate.query(sql, new RowMapper<Integer>() {
            @Override
            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getInt("get_setu_count");
            }
        }, id);
        Integer pp = count.size() == 1 ? count.get(0) : null;
        return count.size() == 1 ? count.get(0) : null;
    }

    public void setSetuCount(Long id, Integer count) {
        String sql = "UPDATE setu_rank_total SET get_setu_count = ? WHERE user_id = ?";
        jdbcTemplate.update(sql, count, id);
    }

    public void insert(Long id, Integer count) {
        String sql = "INSERT INTO setu_rank_total (user_id, get_setu_count) values (?, ?)";
        jdbcTemplate.update(sql, id, count);
    }
}
