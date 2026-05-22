package com.s_giken.training.webapp.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.s_giken.training.webapp.model.entity.ChargeSearchCondition;
import com.s_giken.training.webapp.service.IChargeService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 料金表管理のコントローラークラス
 */
@Controller
@RequestMapping("/charge")
public class ChargeController {
    private final IChargeService service;

    /**
     * 料金表のコントローラークラスのコンストラクタ
     * @param service　料金表のサービスクラスをDI
    */
    public ChargeController(IChargeService service){
        this.service = service;
    }

    /**
     * 料金表検索画面の表示
     * @param charge　Tymeleafに渡すデータ
     * @return　料金表検索画面のHTML
     */
    @GetMapping("/search")
    public String chargeSearch(Model model) {
        var Charge = new ChargeSearchCondition();
        model.addAttribute("charge", Charge);
        return "charge-search-condition";
    }

    /**
     * 料金表検索結果画面の表示
     * @param charge　料金表検索で入力された条件
     * @param model　Tymeleafに渡すデータ
     * @return　料金表検索結果画面のHTML
     */
    @PostMapping("/search")
    public String postMethodName(@ModelAttribute ("charge") ChargeSearchCondition charge, 
        Model model) {
        var list = service.findByNameLike(charge);
        model.addAttribute("list", list);
        return "charge-search-result";
    }
}
