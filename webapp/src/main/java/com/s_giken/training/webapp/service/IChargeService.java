package com.s_giken.training.webapp.service;

import java.util.List;
import java.util.Optional;

import com.s_giken.training.webapp.model.entity.Charge;
import com.s_giken.training.webapp.model.entity.ChargeSearchCondition;

/**
 * 料金表のサービスインターフェイス
 */
public interface IChargeService {

    public List<Charge> findAll();

    public Optional<Charge> findById(Long chargeId);

    public List<Charge> findByNameLike(ChargeSearchCondition charge);

    public void add(Charge charge);

    public void update(Charge charge);
    
    public void deleteId(Long id);
}
