package com.s_giken.training.webapp.service;

import java.util.List;

import com.s_giken.training.webapp.model.entity.Charge;
import com.s_giken.training.webapp.model.entity.ChargeSearchCondition;

/**
 * 料金表のサービスインターフェイス
 */
public interface IChargeService {

    public List<Charge> findAll();

    public List<Charge> findByNameLike(ChargeSearchCondition charge);

}
