package com.s_giken.training.webapp.repository;

import java.util.List;

import com.s_giken.training.webapp.model.entity.Charge;

/**
 * 料金表のリポジトリインターフェイス
 */
public interface IChargeRepository {

    /**
     * 料金情報をすべて取得する
     */
    public List<Charge> findAll();

    /**
     * 料金名の一部にマッチするリストを取得する
     */
    public List<Charge> findByNameLike(String name);
}