package com.s_giken.training.webapp.repository;

import java.util.List;
import java.util.Optional;

import com.s_giken.training.webapp.model.entity.Charge;

/**
 * 料金表のリポジトリインターフェイス
 */
public interface IChargeRepository {

    /**
     * 料金情報をすべて取得する
     */
    public List<Charge> findAll();

    public Optional<Charge> findById(Long id);

    /**
     * 料金名の一部にマッチするリストを取得する
     */
    public List<Charge> findByNameLike(String name);

    public int add(Charge charge);

    public int update(Charge charge);

    public int deleteId(Long id);
}