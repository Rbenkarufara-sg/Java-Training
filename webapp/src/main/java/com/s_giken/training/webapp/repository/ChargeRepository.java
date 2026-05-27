package com.s_giken.training.webapp.repository;

import java.sql.Types;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
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
     * 料金IDをもとに料金情報を取得する
     * @return Optional型のChargeオブジェクト
     */
    @Override
    public Optional<Charge> findById(Long id){
        String sql = "select * from t_charge where charge_id = ?";
        Object[] args = { id };
        int[] argsTypes = {Types.BIGINT};
        Charge charge;
        try{
            charge = jdbcTemplate.queryForObject(sql, args, argsTypes, rowMapper);
        } catch (EmptyResultDataAccessException e){
            charge = null;
        }
        return Optional.ofNullable(charge);
    }
    /**
     * 料金名の一部にマッチするリストを取得する
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

     /**
     * 新規の料金情報を登録する
     * @param charge　DBの追加する値
     * @return 処理した件数
     */
    @Override
    public int add(Charge charge){
        Long chargeId = charge.getChargeId();
        if (chargeId == null){
            chargeId = jdbcTemplate.queryForObject("select nextval('t_charge_seq')", 
            Long.class);
            charge.setChargeId(chargeId);
        }
        String sql = "insert into T_CHARGE " +
        "(charge_id, name, amount, start_date, end_date, created_at, modified_at)" +
        " values (?, ?, ?, ?, ?, current_timestamp, current_timestamp)";

        int processed_count = jdbcTemplate.update(sql, chargeId, 
            charge.getName(), charge.getAmount(), charge.getStartDate(), charge.getEndDate());
        return processed_count;
    }

    /**
     * 料金情報更新
     * @param charge　DBの追加する値
     * @return 処理した件数
     */
    @Override
    public int update(Charge charge){
        String sql = "update T_CHARGE" +
        " set name = ?, amount = ?, start_date = ?, end_date = ?, " +
        " modified_at = current_timestamp " +
        " where charge_id = ?";

        int processed_count = jdbcTemplate.update(
            sql, charge.getName(), charge.getAmount(), charge.getStartDate(), 
            charge.getEndDate(), charge.getChargeId());
        return processed_count;
    }

    /**
     * 料金情報を削除する
     * @param id　削除したいchargeId
     * @return　処理した件数
     */
    @Override
    public int deleteId(Long id){
        String sql = " delete from T_CHARGE where charge_id = ? ";

        int processed_count = jdbcTemplate.update(sql, id);

        return processed_count;
    }


}
