package com.s_giken.training.webapp.repository;

import java.sql.Types;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import com.s_giken.training.webapp.model.entity.Charge;

/**
 * 料金表のリポジトリクラス（実装）
 */
@Repository
public class ChargeRepository implements IChargeRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Charge> rowMapper;

    public ChargeRepository(JdbcTemplate jdbcTemplate, RowMapper<Charge> rowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = rowMapper;
    }

    /**
     * 料金情報をすべて取得する
     * 
     * @return Chargeオブジェクトのリスト
     */
    @Override
    public List<Charge> findAll() {
        String sql = "select * from T_CHARGE";
        List<Charge> result = jdbcTemplate.query(sql, rowMapper);
        return result;
    }

    /**
     * 料金名の一部にマッチするリストを取得する
     * 
     * @return Optional型の Chargeオブジェクト
     */
    @Override
    public List<Charge> findByNameLike(String name) {
        String sql = "select * from T_CHARGE where name like ?";
        Object[] args = {"%" + name + "%"};
        int[] argTypes = {Types.VARCHAR};
        List<Charge> result =jdbcTemplate.query(sql, args, argTypes, rowMapper);
        return result;
    }
}
