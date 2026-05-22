package com.s_giken.training.webapp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.s_giken.training.webapp.model.entity.Charge;
import com.s_giken.training.webapp.model.entity.ChargeSearchCondition;
import com.s_giken.training.webapp.repository.IChargeRepository;

/**
 * 料金表管理のサービスクラス（実装）
 */
@Service
public class ChargeService implements IChargeService {

    private final IChargeRepository repository;

    /**
     * 料金表のサービスクラスのコンストラクタ
     * @param repository　料金表のリポジトリクラスをDI
     */
    public ChargeService(IChargeRepository repository){
        this.repository = repository;
    }

    /**
     * 料金表を全件取得
     * 
     * @return 全料金表
     */
    @Override
    public List<Charge> findAll() {
        return repository.findAll();
    }

    /**
     * 料金表を条件取得
     * 
     * @param charge 料金名検索条件
     * @return 条件に一致した料金情報
     */
    @Override
    public List<Charge> findByNameLike(ChargeSearchCondition charge){
        return repository.findByNameLike(charge.getName());
    }
}
