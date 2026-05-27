package com.s_giken.training.webapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.s_giken.training.webapp.exception.AttributeErrorException;
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
     * @return 全料金表
     */
    @Override
    public List<Charge> findAll() {
        return repository.findAll();
    }

    /**
     * 該当のIDの情報を取得
     * @param chargeId 料金ID
     * @return 料金IDに一致した料金情報
     */
    @Override
    public Optional<Charge> findById(Long chargeId){
        return repository.findById(chargeId);
    }

    /**
     * 料金表を条件取得
     * @param charge 料金名検索条件
     * @return 条件に一致した料金情報
     */
    @Override
    public List<Charge> findByNameLike(ChargeSearchCondition charge){
        return repository.findByNameLike(charge.getName());
    }

    /**
     * 料金情報新規登録
     * @param charge 新規料金情報の入力された値
     */
    @Override
    public void add(Charge charge){
        if (charge.getChargeId() != null){
            throw new AttributeErrorException("料金IDが指定されていると登録できません");
        }
        repository.add(charge);
    }

    /**
     * 更新
     * @param charge 更新で入力された値
     */
    @Override
    public void update(Charge charge){
        if (charge.getChargeId() == null){
            throw new AttributeErrorException("料金IDが指定されていません");
        }
        repository.update(charge);
    }

    /**
     * 削除
     * @param chargeId 削除したいchargeId
     */
    @Override
    public void deleteId(Long chargeId){
        repository.deleteId(chargeId);
    }
}